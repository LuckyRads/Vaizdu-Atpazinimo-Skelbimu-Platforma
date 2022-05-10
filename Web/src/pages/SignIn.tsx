import { LockOutlined } from '@mui/icons-material';
import {
    Avatar,
    Box,
    Button,
    Checkbox,
    Container,
    CssBaseline,
    FormControlLabel,
    Grid,
    Link,
    TextField,
    Typography,
} from '@mui/material';
import { AxiosError } from 'axios';
import React, { FormEvent, ReactElement, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import AuthService from '../authorization/AuthService';
import { LoaderType } from '../types/PropTypes';
import { LoginRequest } from '../types/RequestTypes';
import ErrorUtil from '../utils/ErrorUtil';
import ValidationUtil from '../utils/ValidationUtil';

export default function SignIn(props: LoaderType): ReactElement {
    const navigate = useNavigate();

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [rememberMe, setRememberMe] = useState(false);

    const getUserLoginDetails = (): LoginRequest => {
        return { username, password, rememberMe };
    };

    const handleSubmit = async (event: FormEvent): Promise<void> => {
        event.preventDefault();
        props.showLoader();
        const userLoginDetails = getUserLoginDetails();
        AuthService.login(userLoginDetails)
            .then(() => {
                navigate('/home', { replace: true });
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
                    <LockOutlined />
                </Avatar>
                <Typography component="h1" variant="h5">
                    Sign in
                </Typography>
                <Box component="form" onSubmit={handleSubmit} sx={{ mt: 1 }}>
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        id="login-username"
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
                        name="password"
                        label="Password"
                        type="password"
                        id="login-password"
                        autoComplete="current-password"
                        value={password}
                        onChange={(event): void =>
                            setPassword(event.target.value)
                        }
                    />
                    <FormControlLabel
                        control={
                            <Checkbox
                                value={rememberMe}
                                color="primary"
                                onChange={(): void =>
                                    setRememberMe(!rememberMe)
                                }
                            />
                        }
                        label="Remember me"
                    />
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2 }}
                        disabled={
                            username.length === 0 || password.length === 0
                        }>
                        Sign In
                    </Button>
                    <Grid container alignItems="center" justifyContent="center">
                        {/* <Grid item xs>
                                <Link href="#" variant="body2">
                                    Forgot password?
                                </Link>
                            </Grid> */}
                        <Grid item>
                            <Link href="/sign-up" variant="body2">
                                {"Don't have an account? Sign Up"}
                            </Link>
                        </Grid>
                    </Grid>
                </Box>
            </Box>
        </Container>
    );
}
