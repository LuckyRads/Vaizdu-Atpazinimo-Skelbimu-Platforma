import axios from 'axios';
import CategoryResource from '../../resources/CategoryResource';
import { CategoryDto, GenericResponse } from '../../types/ResponseTypes';

export default class CategoryService {
    public static getCategories = async (): Promise<
        GenericResponse<CategoryDto[]>
    > => {
        const response = await axios.request<GenericResponse<CategoryDto[]>>(
            CategoryResource.getCategories()
        );
        return response.data;
    };
}
