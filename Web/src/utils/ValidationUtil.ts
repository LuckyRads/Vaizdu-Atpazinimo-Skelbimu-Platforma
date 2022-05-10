export default class ValidationUtil {
    /**
     * Username validation.
     *
     * @param username
     * @return true if username is valid
     */
    public static isUsernameValid = (username: string): boolean => {
        if (username.length === 0) return false;
        return true;
    };

    /**
     * RFC2822 Email standard validation.
     *
     * @param email
     * @returns true if email is valid
     */
    public static isEmailValid = (email: string): boolean => {
        if (email.length === 0) return false;
        const regexp =
            /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/;
        return regexp.test(email);
    };

    /**
     * Password is valid when it passes these criteria:
     * - has at least one lower case letter;
     * - has at least one upper case letter;
     * - has at least one digit;
     * - has at least one symbol;
     * - is longer than 7 symbols;
     * - is shorter than 21 symbols.
     *
     * @param password
     * @returns true if password is valid
     */
    public static isPasswordValid = (password: string): boolean => {
        if (password.length === 0) return false;
        const regexpOneLowercase = '(?=.*[a-z])';
        const regexpOneUpperCase = '(?=.*[A-Z])';
        const regexpOneDigit = '(?=.*\\d)';
        const regexpOneSymbol = '(?=.*\\W)';
        const combinedRegexp =
            regexpOneLowercase +
            regexpOneUpperCase +
            regexpOneDigit +
            regexpOneSymbol;
        const regexp = new RegExp(combinedRegexp);
        return (
            regexp.test(password) &&
            password.length >= 8 &&
            password.length <= 20
        );
    };
}
