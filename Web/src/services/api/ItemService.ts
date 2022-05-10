import axios from 'axios';
import ItemResource from '../../resources/ItemResource';
import { FilterRequest } from '../../types/RequestTypes';
import {
    GenericResponse,
    ItemDto,
    StatusResponse,
} from '../../types/ResponseTypes';

export default class ItemService {
    public static getItems = async (
        categoryId: number | undefined
    ): Promise<GenericResponse<ItemDto[]>> => {
        const response = await axios.request<GenericResponse<ItemDto[]>>(
            ItemResource.getItems(categoryId)
        );
        return response.data;
    };

    public static getOwnedItems = async (
        categoryId: number | undefined,
        filterRequest: FilterRequest | undefined
    ): Promise<GenericResponse<ItemDto[]>> => {
        const response = await axios.request<GenericResponse<ItemDto[]>>(
            ItemResource.getOwnedItems(categoryId, filterRequest)
        );
        return response.data;
    };

    public static getFilteredItems = async (
        categoryId: number | undefined,
        filterRequest: FilterRequest
    ): Promise<GenericResponse<ItemDto[]>> => {
        const response = await axios.request<GenericResponse<ItemDto[]>>(
            ItemResource.getFilteredItems(categoryId, filterRequest)
        );
        return response.data;
    };

    public static getItem = async (
        id: number
    ): Promise<GenericResponse<ItemDto>> => {
        const response = await axios.request<GenericResponse<ItemDto>>(
            ItemResource.getItem(id)
        );
        return response.data;
    };

    public static createItem = async (
        item: ItemDto
    ): Promise<StatusResponse> => {
        const response = await axios.request<StatusResponse>(
            ItemResource.createItem(item)
        );
        return response.data;
    };

    public static updateItem = async (
        id: string,
        item: ItemDto
    ): Promise<StatusResponse> => {
        const response = await axios.request<StatusResponse>(
            ItemResource.updateItem(id, item)
        );
        return response.data;
    };

    public static deleteItem = async (id: string): Promise<StatusResponse> => {
        const response = await axios.request<StatusResponse>(
            ItemResource.deleteItem(id)
        );
        return response.data;
    };
}
