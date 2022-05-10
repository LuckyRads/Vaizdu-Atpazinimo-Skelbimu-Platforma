export type RegisterRequest = {
    username: string;
    email: string;
    password: string;
    validationPassword: string;
};

export type LoginRequest = {
    username: string;
    password: string;
    rememberMe: boolean;
};

export type FilterRequest = {
    fromPrice: string;
    toPrice: string;
};
