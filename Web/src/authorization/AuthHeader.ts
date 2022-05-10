import { AuthHeader, JwtResponse } from '../types/AuthTypes';

const authHeader = (): AuthHeader => {
    const userString = localStorage.getItem('user');
    let user: JwtResponse | null = null;
    if (userString) user = JSON.parse(userString);
    if (user && user.token) {
        return { Authorization: `Bearer ${user.token}` };
    }
    return {};
};

export default authHeader;
