import { AxiosRequestConfig } from 'axios';
import authHeader from '../authorization/AuthHeader';
import ResourceConfig from '../configs/ResourceConfig';

export default class ImageResource {
    public static uploadImage = (imageFile: FormData): AxiosRequestConfig => ({
        headers: authHeader(),
        method: 'POST',
        url: `${ResourceConfig.API_URL}/api/images`,
        data: imageFile,
    });

    public static deleteImage = (id: number): AxiosRequestConfig => ({
        headers: authHeader(),
        method: 'DELETE',
        url: `${ResourceConfig.API_URL}/api/images/${id}`,
    });
}
