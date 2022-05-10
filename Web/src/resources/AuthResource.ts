import { AxiosRequestConfig } from 'axios';
import ResourceConfig from '../configs/ResourceConfig';
import { LoginRequest, RegisterRequest } from '../types/RequestTypes';

export default class AuthResource {
    public static register = (
        registerRequest: RegisterRequest
    ): AxiosRequestConfig => ({
        method: 'POST',
        url: `${ResourceConfig.API_URL}/api/auth/register`,
        data: registerRequest,
    });

    public static login = (loginRequest: LoginRequest): AxiosRequestConfig => ({
        method: 'POST',
        url: `${ResourceConfig.API_URL}/api/auth/login`,
        data: loginRequest,
    });
}
