import { AxiosRequestConfig } from 'axios';
import authHeader from '../authorization/AuthHeader';
import ResourceConfig from '../configs/ResourceConfig';

export default class CategoryResource {
    public static getCategories = (): AxiosRequestConfig => ({
        headers: authHeader(),
        method: 'GET',
        url: `${ResourceConfig.API_URL}/api/categories`,
    });
}
