import {
    Box,
    Button,
    Divider,
    InputAdornment,
    InputLabel,
    MenuItem,
    OutlinedInput,
    Select,
    SelectChangeEvent,
    Typography,
} from '@mui/material';
import { AxiosError } from 'axios';
import React, { FormEvent, ReactElement, useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import AuthService from '../authorization/AuthService';
import { CenteredBox, FlexBox } from '../components/Boxes/Boxes';
import ImageUpload from '../components/ImageUpload/ImageUpload';
import { FlexFormControl, FlexTextField } from '../components/Inputs/Inputs';
import CategoryService from '../services/api/CategoryService';
import ItemService from '../services/api/ItemService';
import { LoaderType } from '../types/PropTypes';
import { ImageResponse, ItemDto } from '../types/ResponseTypes';
import ErrorUtil from '../utils/ErrorUtil';

export default function ItemModification(props: LoaderType): ReactElement {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [category, setCategory] = useState('');
    const [categories, setCategories] = useState<string[]>([]);
    const [images, setImages] = useState<ImageResponse[]>([]);
    const [owner, setOwner] = useState('');
    const [contactNumber, setContactNumber] = useState('');
    const [price, setPrice] = useState('');

    const navigate = useNavigate();
    const { id } = useParams();
    const [isItemLoaded, setIsItemLoaded] = useState(false);
    const [isCurrentUserOwner, setIsCurrentUserOwner] = useState(false);
    const [isModification, setIsModification] = useState(false);

    useEffect(() => {
        if (id === undefined) setCategory(images[0]?.category);
    }, [id, images]);

    useEffect(() => {
        setIsModification(id !== undefined);
        const currentUser = AuthService.getCurrentUser();
        if (id === undefined && currentUser !== null) {
            setOwner(currentUser.username);
        } else if (id !== undefined) {
            const idNumber = Number(id);
            props.showLoader();
            ItemService.getItem(idNumber)
                .then(response => {
                    setName(response.data.name);
                    setDescription(response.data.description);
                    setCategory(response.data.category);
                    setOwner(response.data.owner);
                    if (response.data.images !== undefined) {
                        setImages(response.data.images);
                        setIsItemLoaded(true);
                    }
                    setContactNumber(response.data.contactNumber);
                    setPrice(response.data.price);
                })
                .catch((error: AxiosError) => {
                    toast.error(ErrorUtil.getAxiosErrorMessage(error));
                })
                .finally(() => props.hideLoader());
        } else {
            navigate('/home');
        }

        CategoryService.getCategories()
            .then(response => {
                const responseCategories = response.data.map(
                    resCategory => resCategory.name
                );
                setCategories(responseCategories);
            })
            .catch(null);
    }, [id, navigate]);

    useEffect(() => {
        setIsCurrentUserOwner(AuthService.getCurrentUser()?.username === owner);
    }, [owner, AuthService.getCurrentUser()?.username]);

    const getItemDetails = (): ItemDto => {
        return {
            name,
            description,
            category,
            images,
            owner,
            contactNumber,
            price,
        };
    };

    const handleSubmit = (event: FormEvent): void => {
        event.preventDefault();
        props.showLoader();
        if (isModification && id !== undefined) {
            ItemService.updateItem(id, getItemDetails())
                .then(response => {
                    toast.success(response.message);
                })
                .catch((error: AxiosError) => {
                    toast.error(ErrorUtil.getAxiosErrorMessage(error));
                })
                .finally(() => props.hideLoader());
        } else {
            ItemService.createItem(getItemDetails())
                .then(response => {
                    toast.success(response.message);
                })
                .catch((error: AxiosError) => {
                    toast.error(ErrorUtil.getAxiosErrorMessage(error));
                })
                .finally(() => props.hideLoader());
        }
    };

    const handleDelete = (event: React.MouseEvent<HTMLElement>): void => {
        event.preventDefault();
        if (isModification) {
            if (id === undefined) return;
            props.showLoader();
            ItemService.deleteItem(id)
                .then(response => {
                    toast.success(response.message);
                    navigate('/home');
                })
                .catch((error: AxiosError) => {
                    toast.error(ErrorUtil.getAxiosErrorMessage(error));
                })
                .finally(() => props.hideLoader());
        } else {
            navigate('/home');
        }
    };

    const handleCategoryChange = (event: SelectChangeEvent): void => {
        setCategory(event.target.value);
    };

    return (
        <CenteredBox component="form" onSubmit={handleSubmit}>
            <Typography
                component="h1"
                variant="h5"
                align="left"
                sx={{ margin: '1em' }}>
                {isCurrentUserOwner && !isModification ? 'Item creation' : ''}
                {isCurrentUserOwner && isModification
                    ? 'Item modification'
                    : ''}
                {!isCurrentUserOwner ? name : ''}
            </Typography>
            <FlexBox>
                <FlexBox width="100%">
                    {isCurrentUserOwner ? (
                        <FlexTextField
                            required
                            label="Item name"
                            name="item-name"
                            autoFocus
                            value={name}
                            onChange={(event): void =>
                                setName(event.target.value)
                            }
                        />
                    ) : null}
                    <FlexFormControl>
                        <InputLabel htmlFor="outlined-adornment-amount">
                            Price
                        </InputLabel>
                        <OutlinedInput
                            id="outlined-adornment-amount"
                            required
                            disabled={!isCurrentUserOwner}
                            value={price}
                            onChange={(event): void => {
                                setPrice(event.target.value);
                            }}
                            startAdornment={
                                <InputAdornment position="start">
                                    €
                                </InputAdornment>
                            }
                            label="Price"
                        />
                    </FlexFormControl>
                    <FlexFormControl
                        sx={{ flexGrow: 1, width: '200px' }}
                        disabled={!isCurrentUserOwner}>
                        <InputLabel id="select-category">Category</InputLabel>
                        <Select
                            disabled={!isCurrentUserOwner}
                            labelId="select-category"
                            id="category-select"
                            value={category}
                            label="Category"
                            onChange={handleCategoryChange}>
                            {categories.map(categoryMenuItem => (
                                <MenuItem
                                    key={categoryMenuItem}
                                    value={categoryMenuItem}>
                                    {categoryMenuItem}
                                </MenuItem>
                            ))}
                        </Select>
                    </FlexFormControl>
                </FlexBox>
                <FlexBox width="100%">
                    <FlexTextField
                        required
                        multiline
                        disabled={!isCurrentUserOwner}
                        label="Description"
                        name="item-description"
                        value={description}
                        onChange={(event): void =>
                            setDescription(event.target.value)
                        }
                    />
                </FlexBox>
                <FlexBox width="100%">
                    <FlexTextField
                        disabled
                        label="Owner"
                        name="item-owner"
                        value={owner}
                    />
                    <FlexTextField
                        required
                        disabled={!isCurrentUserOwner}
                        label="Contact number"
                        name="contact-number"
                        value={contactNumber}
                        onChange={(event): void =>
                            setContactNumber(event.target.value)
                        }
                    />
                </FlexBox>
                <Box width="100%" margin="1em">
                    <Divider />
                    <Typography component="h2" variant="h6">
                        Images
                    </Typography>
                    <ImageUpload
                        isCurrentUserOwner={isCurrentUserOwner}
                        isItemLoaded={isItemLoaded}
                        images={images}
                        setImages={setImages}
                        showLoader={props.showLoader}
                        hideLoader={props.hideLoader}
                    />
                    <Divider />
                </Box>
                {isCurrentUserOwner ? (
                    <Box width="100%" justifyContent="flex-start">
                        <Button
                            variant="contained"
                            type="submit"
                            sx={{ marginInline: '1em' }}>
                            {!isModification ? 'Create' : 'Update'} item
                        </Button>
                        <Button
                            variant="contained"
                            color="error"
                            onClick={handleDelete}
                            sx={{ marginInline: '1em' }}>
                            {isModification ? 'Delete item' : 'Cancel'}
                        </Button>
                    </Box>
                ) : (
                    ''
                )}
            </FlexBox>
        </CenteredBox>
    );
}
