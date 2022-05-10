import axios from 'axios';
import ImageResource from '../../resources/ImageResource';
import {
    GenericResponse,
    ImageResponse,
    StatusResponse,
} from '../../types/ResponseTypes';

export default class ImageService {
    public static uploadImage = async (
        imageFile: FormData
    ): Promise<GenericResponse<ImageResponse>> => {
        const response = await axios.request<GenericResponse<ImageResponse>>(
            ImageResource.uploadImage(imageFile)
        );
        return response.data;
    };

    public static deleteImage = async (id: number): Promise<StatusResponse> => {
        const response = await axios.request<StatusResponse>(
            ImageResource.deleteImage(id)
        );
        return response.data;
    };
}
