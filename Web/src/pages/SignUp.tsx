import { LockOpen } from '@mui/icons-material';
import {
    Avatar,
    Box,
    Button,
    Container,
    CssBaseline,
    Grid,
    Link,
    TextField,
    Typography,
} from '@mui/material';
import { AxiosError } from 'axios';
import React, { FormEvent, ReactElement, useState } from 'react';
import { toast } from 'react-toastify';
import AuthService from '../authorization/AuthService';
import { LoaderType } from '../types/PropTypes';
import { RegisterRequest } from '../types/RequestTypes';
import ErrorUtil from '../utils/ErrorUtil';
import ValidationUtil from '../utils/ValidationUtil';

export default function SignUp(props: LoaderType): ReactElement {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [validationPassword, setValidationPassword] = useState('');

    const areRegistrationDetailsValid = (): boolean => {
        return (
            ValidationUtil.isUsernameValid(username) &&
            ValidationUtil.isEmailValid(email) &&
            ValidationUtil.isPasswordValid(password) &&
            password === validationPassword
        );
    };

    const getUserRegistrationDetails = (): RegisterRequest => {
        if (!ValidationUtil.isUsernameValid(username))
            throw new Error('Invalid username');
        if (!ValidationUtil.isEmailValid(email))
            throw new Error('Email can not be empty.');
        if (!ValidationUtil.isPasswordValid(password))
            throw new Error('Password can not be empty.');
        if (password !== validationPassword)
            throw new Error('Both passwords must math.');

        return { username, email, password, validationPassword };
    };

    const handleSubmit = async (event: FormEvent): Promise<void> => {
        event.preventDefault();
        props.showLoader();
        const userRegistrationDetails = getUserRegistrationDetails();
        AuthService.register(userRegistrationDetails)
            .then(response => {
                toast.success(response.message);
            })
            .catch((error: AxiosError) => {
                toast.error(ErrorUtil.getAxiosErrorMessage(error));
            })
            .finally(() => props.hideLoader());
    };

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <Box
                sx={{
                    marginTop: 8,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                }}>
                <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
                    <LockOpen />
                </Avatar>
                <Typography component="h1" variant="h5">
                    Sign up
                </Typography>
                <Box component="form" onSubmit={handleSubmit} sx={{ mt: 1 }}>
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        id="register-username"
                        label="Username"
                        name="username"
                        autoFocus
                        value={username}
                        onChange={(event): void =>
                            setUsername(event.target.value)
                        }
                        error={!ValidationUtil.isUsernameValid(username)}
                    />
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        id="register-email"
                        label="Email Address"
                        name="email"
                        autoComplete="email"
                        autoFocus
                        value={email}
                        onChange={(event): void => setEmail(event.target.value)}
                        error={!ValidationUtil.isEmailValid(email)}
                    />
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        name="password"
                        label="Password"
                        type="password"
                        id="register-password"
                        autoComplete="current-password"
                        value={password}
                        onChange={(event): void =>
                            setPassword(event.target.value)
                        }
                        error={!ValidationUtil.isPasswordValid(password)}
                        helperText={
                            !ValidationUtil.isPasswordValid(password)
                                ? 'Password should contain at least 8 characters.'
                                : ''
                        }
                    />
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        name="validation-password"
                        label="Repeat password"
                        type="password"
                        id="validation-password"
                        autoComplete="current-password"
                        value={validationPassword}
                        onChange={(event): void =>
                            setValidationPassword(event.target.value)
                        }
                        error={password !== validationPassword}
                        helperText={
                            password !== validationPassword
                                ? 'Password and validation password must be the same.'
                                : ''
                        }
                    />
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        disabled={!areRegistrationDetailsValid()}
                        sx={{ mt: 3, mb: 2 }}>
                        Sign Up
                    </Button>
                    <Grid container alignItems="center" justifyContent="center">
                        {/* <Grid item xs>
                                <Link href="#" variant="body2">
                                    Forgot password?
                                </Link>
                            </Grid> */}
                        <Grid item>
                            <Link href="/sign-in" variant="body2">
                                {'Have an account? Sign In'}
                            </Link>
                        </Grid>
                    </Grid>
                </Box>
            </Box>
        </Container>
    );
}
