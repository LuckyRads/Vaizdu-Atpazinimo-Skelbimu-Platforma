import { Typography } from '@mui/material';
import { AxiosError } from 'axios';
import React, { Fragment, ReactElement, useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import Filters from '../components/Filters/Filters';
import ItemList from '../components/ItemList/ItemList';
import CategoryService from '../services/api/CategoryService';
import ItemService from '../services/api/ItemService';
import { LoaderType } from '../types/PropTypes';
import { CategoryDto, ItemDto } from '../types/ResponseTypes';
import ErrorUtil from '../utils/ErrorUtil';

type PropsType = {
    isOwnedItems: boolean | undefined;
};

export default function Home(props: PropsType & LoaderType): ReactElement {
    const [items, setItems] = useState<ItemDto[]>([]);
    const [category, setCategory] = useState<CategoryDto>();
    const [categories, setCategories] = useState<CategoryDto[]>([]);

    useEffect(() => {
        CategoryService.getCategories()
            .then(response => {
                setCategories(response.data);
                const defaultCategory = response.data
                    .filter(cat => cat.parentCategory === null)
                    .at(0);
                if (defaultCategory !== undefined) setCategory(defaultCategory);
            })
            .catch((error: AxiosError) => {
                toast.error(ErrorUtil.getAxiosErrorMessage(error));
            });
    }, []);

    useEffect(() => {
        props.showLoader();
        if (props.isOwnedItems) {
            ItemService.getOwnedItems(category?.id, undefined)
                .then(response => {
                    setItems(response.data);
                })
                .catch(null)
                .finally(() => props.hideLoader());
        } else {
            ItemService.getItems(category?.id)
                .then(response => {
                    setItems(response.data);
                })
                .catch(null)
                .finally(() => props.hideLoader());
        }
    }, [category, props.isOwnedItems]);

    return (
        <Fragment>
            <Typography align="center" component="h4" variant="h4">
                {props.isOwnedItems ? 'Owned items' : 'Existing items'}
            </Typography>
            <Filters
                setItems={setItems}
                setCategory={setCategory}
                category={category}
                categories={categories}
                isOwnedItems={props.isOwnedItems}
            />
            {items.length !== 0 ? (
                <Fragment>
                    <ItemList items={items} />
                </Fragment>
            ) : (
                <Typography align="center" component="h6" variant="h6">
                    There are no existing items for the current criteria.
                </Typography>
            )}
        </Fragment>
    );
}
