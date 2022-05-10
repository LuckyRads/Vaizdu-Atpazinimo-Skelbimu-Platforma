import DeleteForeverIcon from '@mui/icons-material/DeleteForever';
import UploadFileIcon from '@mui/icons-material/UploadFile';
import { Box, Card, CardContent, Grid, IconButton } from '@mui/material';
import { AxiosError } from 'axios';
import React, {
    ChangeEvent,
    Dispatch,
    ReactElement,
    SetStateAction,
    useEffect,
    useState,
} from 'react';
import { toast } from 'react-toastify';
import ImageService from '../../services/api/ImageService';
import { LoaderType } from '../../types/PropTypes';
import { ImageResponse } from '../../types/ResponseTypes';
import ErrorUtil from '../../utils/ErrorUtil';
import ImageBox from '../Containers/ImageBox';
import { TruncatedTypography } from '../TextElements/TextElements';

type PropsType = {
    images: ImageResponse[];
    isCurrentUserOwner: boolean;
    isItemLoaded: boolean;
    setImages: Dispatch<SetStateAction<ImageResponse[]>>;
};

export default function ImageUpload(
    props: PropsType & LoaderType
): ReactElement {
    const [uploadedImages, setUploadedImages] = useState<ImageResponse[]>([]);

    useEffect(() => {
        props.setImages(uploadedImages);
    }, [props, uploadedImages]);

    useEffect(() => {
        setUploadedImages(props.images);
    }, [props.isItemLoaded]);

    const handleFileInputChange = (
        event: ChangeEvent<HTMLInputElement>
    ): void => {
        if (event.target.files === null) return;

        for (const file of event.target.files) {
            const formData = new FormData();
            formData.append('imageFile', file);

            props.showLoader();
            ImageService.uploadImage(formData)
                .then(response => {
                    setUploadedImages(oldImages => [
                        ...oldImages,
                        response.data,
                    ]);
                })
                .catch((error: AxiosError) => {
                    toast.error(ErrorUtil.getAxiosErrorMessage(error));
                })
                .finally(() => props.hideLoader());
        }
    };

    const handleImageDelete = (id: number): void => {
        props.showLoader();
        ImageService.deleteImage(id)
            .then(response => {
                setUploadedImages(oldImages =>
                    oldImages.filter(image => image.id !== id)
                );
                toast.success(response.message);
            })
            .catch((error: AxiosError) => {
                toast.error(ErrorUtil.getAxiosErrorMessage(error));
            })
            .finally(() => props.hideLoader());
    };

    return (
        <Box marginTop={2} marginBottom={2}>
            <Grid container rowSpacing={2} columnSpacing={2}>
                {uploadedImages.map(uploadedImage => (
                    <Grid key={uploadedImage.name} item>
                        <Card
                            sx={{
                                width: 250,
                                height: 250,
                                display: 'flex',
                                justifyContent: 'center',
                                alignContent: 'center',
                            }}>
                            <CardContent>
                                <Box
                                    width="100%"
                                    display="inline-flex"
                                    justifyContent="center">
                                    <TruncatedTypography
                                        width="180px"
                                        align="center">
                                        {uploadedImage.name}
                                    </TruncatedTypography>
                                    {props.isCurrentUserOwner ? (
                                        <IconButton
                                            sx={{
                                                marginTop: '-10px',
                                                marginRight: '-10px',
                                            }}
                                            onClick={(): void =>
                                                handleImageDelete(
                                                    uploadedImage.id
                                                )
                                            }>
                                            <DeleteForeverIcon />
                                        </IconButton>
                                    ) : (
                                        ''
                                    )}
                                </Box>
                                <Box width="100%">
                                    <ImageBox data={uploadedImage.data} />
                                </Box>
                            </CardContent>
                        </Card>
                    </Grid>
                ))}
                {props.isCurrentUserOwner ? (
                    <IconButton
                        size="large"
                        sx={{
                            marginBottom: 2,
                            cursor: 'pointer',
                            height: '3em',
                            width: '3em',
                            margin: '0.5em',
                            display: 'flex',
                            justifyContent: 'center',
                            alignContent: 'center',
                            alignItems: 'center',
                        }}>
                        <label htmlFor="image-upload-input">
                            <input
                                id="image-upload-input"
                                type="file"
                                accept="image/**"
                                multiple
                                onChange={(event): void =>
                                    handleFileInputChange(event)
                                }
                                hidden
                            />
                            <UploadFileIcon
                                sx={{
                                    cursor: 'pointer',
                                    height: '2em',
                                    width: '2em',
                                }}
                            />
                        </label>
                    </IconButton>
                ) : null}
            </Grid>
        </Box>
    );
}
