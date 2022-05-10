import { Grid } from '@mui/material';
import React, { ReactElement, useEffect, useState } from 'react';
import { ItemDto } from '../../types/ResponseTypes';
import ItemListing from '../ItemListing/ItemListing';

export default function ItemList(props: { items: ItemDto[] }): ReactElement {
    const [items, setItems] = useState<ItemDto[]>([]);

    useEffect(() => {
        setItems(props.items);
    }, [props.items]);

    return (
        <Grid
            container
            rowSpacing={2}
            columnSpacing={2}
            sx={{ m: 2, width: '95vw' }}>
            {items.map(item => (
                <ItemListing key={item.name} item={item} />
            ))}
        </Grid>
    );
}
