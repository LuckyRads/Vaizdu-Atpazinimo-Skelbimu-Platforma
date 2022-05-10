import { Card, CardContent, Grid, Typography } from '@mui/material';
import React, { ReactElement, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { ItemDto } from '../../types/ResponseTypes';
import ImageBox from '../Containers/ImageBox';
import { TruncatedTypography } from '../TextElements/TextElements';

export default function ItemListing(props: { item: ItemDto }): ReactElement {
    const navigate = useNavigate();
    const [imageData, setImageData] = useState('');

    useEffect(() => {
        if (props.item.images !== undefined && props.item.images.length > 0) {
            setImageData(props.item.images[0].data);
        }
    }, [props]);

    const handleOnClick = (): void => {
        navigate(`/item/${props.item.id}`);
    };

    return (
        <Grid item sx={{ width: '300px' }}>
            <Card
                sx={{
                    width: '100%',
                    display: 'inline-flex',
                    justifyContent: 'center',
                    cursor: 'pointer',
                }}
                onClick={handleOnClick}>
                <CardContent>
                    <TruncatedTypography width="180px" align="center">
                        {props.item.name}
                    </TruncatedTypography>
                    <TruncatedTypography
                        width="180px"
                        color="secondary"
                        align="center">
                        {props.item.category}
                    </TruncatedTypography>
                    <ImageBox data={imageData} />
                    <Typography color="primary" align="center">
                        {props.item.price} €
                    </Typography>
                </CardContent>
            </Card>
        </Grid>
    );
}
