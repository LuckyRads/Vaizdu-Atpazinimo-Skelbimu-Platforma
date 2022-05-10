export type StatusCode = 'SUCCESS' | 'PARTIAL_SUCCESS' | 'FAILED';

export type StatusResponse = {
    status: StatusCode;
    message: string | null;
};

export type GenericResponse<T> = StatusResponse & {
    data: T;
};

export type ItemDto = {
    id?: number;
    name: string;
    description: string;
    category: string;
    images?: ImageResponse[];
    owner: string;
    contactNumber: string;
    price: string;
};

export type ImageResponse = {
    id: number;
    name: string;
    data: string;
    category: string;
};

export type CategoryDto = {
    id: number;
    name: string;
    parentCategory: CategoryDto | null;
    subcategories: CategoryDto[];
};
