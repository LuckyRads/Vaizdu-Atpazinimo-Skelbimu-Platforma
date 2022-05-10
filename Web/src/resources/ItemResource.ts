import { AxiosRequestConfig } from 'axios';
import authHeader from '../authorization/AuthHeader';
import ResourceConfig from '../configs/ResourceConfig';
import { FilterRequest } from '../types/RequestTypes';
import { ItemDto } from '../types/ResponseTypes';

export default class ItemResource {
    public static getItems = (
        categoryId: number | undefined
    ): AxiosRequestConfig => ({
        headers: authHeader(),
        method: 'GET',
        url: `${ResourceConfig.API_URL}/api/items`,
        params: { categoryId },
    });

    public static getOwnedItems = (
        categoryId: number | undefined,
        filterRequest: FilterRequest | undefined
    ): AxiosRequestConfig => ({
        headers: authHeader(),
        method: 'GET',
        url: `${ResourceConfig.API_URL}/api/items/owned`,
        params: { categoryId, ...filterRequest },
    });

    public static getFilteredItems = (
        categoryId: number | undefined,
        filterRequest: FilterRequest
    ): AxiosRequestConfig => ({
        headers: authHeader(),
        method: 'GET',
        url: `${ResourceConfig.API_URL}/api/items/filtered`,
        params: { ...filterRequest, categoryId },
    });

    public static getItem = (id: number): AxiosRequestConfig => ({
        headers: authHeader(),
        method: 'GET',
        url: `${ResourceConfig.API_URL}/api/items/${id}`,
    });

    public static createItem = (item: ItemDto): AxiosRequestConfig => ({
        headers: authHeader(),
        method: 'POST',
        url: `${ResourceConfig.API_URL}/api/items`,
        data: item,
    });

    public static updateItem = (
        id: string,
        item: ItemDto
    ): AxiosRequestConfig => ({
        headers: authHeader(),
        method: 'PUT',
        url: `${ResourceConfig.API_URL}/api/items/${id}`,
        data: item,
    });

    public static deleteItem = (id: string): AxiosRequestConfig => ({
        headers: authHeader(),
        method: 'DELETE',
        url: `${ResourceConfig.API_URL}/api/items/${id}`,
    });
}
