import axios from 'axios';
import AuthResource from '../resources/AuthResource';
import { JwtResponse } from '../types/AuthTypes';
import { LoginRequest, RegisterRequest } from '../types/RequestTypes';
import { GenericResponse, StatusResponse } from '../types/ResponseTypes';

export default class AuthService {
    public static register = async (
        registerRequest: RegisterRequest
    ): Promise<StatusResponse> => {
        const response = await axios.request<StatusResponse>(
            AuthResource.register(registerRequest)
        );
        return response.data;
    };

    public static login = async (
        loginRequest: LoginRequest
    ): Promise<GenericResponse<JwtResponse>> => {
        const response = await axios.request<GenericResponse<JwtResponse>>(
            AuthResource.login(loginRequest)
        );
        if (response.data.data.token) {
            localStorage.setItem('user', JSON.stringify(response.data.data));
        }
        return response.data;
    };

    public static logout = (): void => {
        localStorage.removeItem('user');
    };

    public static getCurrentUser = (): JwtResponse | null => {
        const userString = localStorage.getItem('user');
        return userString ? JSON.parse(userString) : null;
    };
}
