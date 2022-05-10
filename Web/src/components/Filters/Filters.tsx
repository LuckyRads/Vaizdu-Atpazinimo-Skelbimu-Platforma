import {
    Box,
    Button,
    InputAdornment,
    InputLabel,
    MenuItem,
    OutlinedInput,
    Select,
    SelectChangeEvent,
    Typography,
} from '@mui/material';
import { AxiosError } from 'axios';
import React, { Dispatch, FormEvent, ReactElement, useState } from 'react';
import { toast } from 'react-toastify';
import ItemService from '../../services/api/ItemService';
import { FilterRequest } from '../../types/RequestTypes';
import { CategoryDto, ItemDto } from '../../types/ResponseTypes';
import ErrorUtil from '../../utils/ErrorUtil';
import { FlexBox } from '../Boxes/Boxes';
import { FlexFormControl } from '../Inputs/Inputs';

type PropsType = {
    setItems: Dispatch<ItemDto[]>;
    setCategory: Dispatch<CategoryDto | undefined>;
    category: CategoryDto | undefined;
    categories: CategoryDto[];
    isOwnedItems: boolean | undefined;
};

export default function Filters(props: PropsType): ReactElement {
    const [fromPrice, setFromPrice] = useState('');
    const [toPrice, setToPrice] = useState('');

    const getFilterRequest = (): FilterRequest => ({
        fromPrice,
        toPrice,
    });

    const handleSubmit = (event: FormEvent | undefined): void => {
        if (event !== undefined) event.preventDefault();
        if (props.isOwnedItems) {
            ItemService.getOwnedItems(props.category?.id, getFilterRequest())
                .then(response => {
                    props.setItems(response.data);
                })
                .catch(null);
        } else {
            ItemService.getFilteredItems(props.category?.id, getFilterRequest())
                .then(response => {
                    props.setItems(response.data);
                })
                .catch((error: AxiosError) => {
                    toast.error(ErrorUtil.getAxiosErrorMessage(error));
                });
        }
    };

    const handleCategoryChange = (event: SelectChangeEvent): void => {
        const category = props.categories
            .filter(cat => cat.id === Number(event.target.value))
            .at(0);
        props.setCategory(category);
    };

    const handleCleanFilters = (event: React.MouseEvent<HTMLElement>): void => {
        event.preventDefault();
        setFromPrice('');
        setToPrice('');
        handleSubmit(undefined);
    };

    return (
        <FlexBox component="form" onSubmit={handleSubmit}>
            <Box
                width="100%"
                display="flex"
                justifyContent="flex-start"
                alignItems="flex-start"
                margin="1em">
                <FlexFormControl sx={{ minWidth: 250 }}>
                    <InputLabel id="select-category">Category</InputLabel>
                    <Select
                        labelId="select-category"
                        id="category-select"
                        defaultValue="All"
                        value={props.category?.name}
                        label="Category"
                        onChange={handleCategoryChange}>
                        {props.categories.map(categoryMenuItem => (
                            <MenuItem
                                key={categoryMenuItem.name}
                                value={categoryMenuItem.id}>
                                {categoryMenuItem.name}
                            </MenuItem>
                        ))}
                    </Select>
                </FlexFormControl>
            </Box>
            <Box width="100%" sx={{ marginInline: '2em' }}>
                <Typography width="100%">Filter by price</Typography>
                <Box
                    width="100%"
                    justifyContent="flex-start"
                    alignItems="flex-start">
                    <FlexFormControl>
                        <InputLabel htmlFor="from-price">From</InputLabel>
                        <OutlinedInput
                            id="from-price"
                            value={fromPrice}
                            onChange={(event): void => {
                                setFromPrice(event.target.value);
                            }}
                            startAdornment={
                                <InputAdornment position="start">
                                    €
                                </InputAdornment>
                            }
                            label="From"
                        />
                    </FlexFormControl>
                    <FlexFormControl>
                        <InputLabel htmlFor="to-price">To</InputLabel>
                        <OutlinedInput
                            id="to-price"
                            value={toPrice}
                            onChange={(event): void => {
                                setToPrice(event.target.value);
                            }}
                            startAdornment={
                                <InputAdornment position="start">
                                    €
                                </InputAdornment>
                            }
                            label="To"
                        />
                    </FlexFormControl>
                </Box>
                <Button
                    variant="contained"
                    type="submit"
                    sx={{ margin: '0.5em' }}>
                    Filter
                </Button>
                <Button
                    variant="contained"
                    color="secondary"
                    sx={{ margin: '0.5em' }}
                    onClick={handleCleanFilters}>
                    Clean
                </Button>
            </Box>
        </FlexBox>
    );
}
