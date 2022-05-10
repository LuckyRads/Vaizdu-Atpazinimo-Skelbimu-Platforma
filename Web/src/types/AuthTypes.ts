export type AuthHeader = {
    Authorization?: string;
};

export type JwtResponse = {
    token: string;
    type: string;
    id: number;
    username: string;
    email: string;
    roles: string[];
};
