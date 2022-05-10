import { AxiosError } from 'axios';
import { StatusResponse } from '../types/ResponseTypes';

export default class ErrorUtil {
    public static getAxiosErrorMessage = (error: AxiosError): string => {
        const bodyErrorMessage = error.response?.data;
        if (
            this.#isStatusResponse(bodyErrorMessage) &&
            bodyErrorMessage.message !== null
        )
            return bodyErrorMessage.message;
        return error.message;
    };

    static #isStatusResponse = (
        element: unknown
    ): element is StatusResponse => {
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        return (element as StatusResponse).message !== undefined;
    };
}
