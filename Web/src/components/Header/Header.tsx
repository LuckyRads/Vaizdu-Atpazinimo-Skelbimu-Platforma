import { AccountCircle, Login } from '@mui/icons-material';
import HomeIcon from '@mui/icons-material/Home';
import { AppBar, Box, Button, IconButton, Toolbar } from '@mui/material';
import React, { ReactElement, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthService from '../../authorization/AuthService';
import ProfileMenu from './ProfileMenu';

export default function Header(): ReactElement {
    const navigate = useNavigate();

    const [isLoggedIn, setIsLoggedIn] = useState<boolean | null>(null);
    const [profileMenuAnchor, setProfileMenuAnchor] =
        useState<HTMLElement | null>(null);

    useEffect(() => {
        setIsLoggedIn(AuthService.getCurrentUser() !== null);
    }, []);

    const handleLogin = (): void => {
        setIsLoggedIn(true);
        navigate('/sign-in', { replace: true });
    };

    const handleProfileMenuOpen = (
        event: React.MouseEvent<HTMLElement>
    ): void => {
        setProfileMenuAnchor(event.currentTarget);
    };

    const handleNavigateToSell = (): void => {
        if (isLoggedIn) {
            navigate('/item');
        } else {
            navigate('/sign-in');
        }
    };

    return (
        <Box sx={{ flexGrow: 1 }}>
            <AppBar position="static">
                <Toolbar>
                    <Box>
                        <IconButton
                            size="large"
                            edge="start"
                            color="inherit"
                            onClick={(): void => navigate('/home')}
                            sx={{ mr: 2 }}>
                            <HomeIcon />
                        </IconButton>
                    </Box>
                    <Box>
                        <Button
                            variant="contained"
                            color="info"
                            onClick={handleNavigateToSell}>
                            Sell
                        </Button>
                    </Box>
                    <Box sx={{ flexGrow: 1 }} />
                    <Box>
                        {isLoggedIn ? (
                            <IconButton
                                size="large"
                                edge="end"
                                onClick={handleProfileMenuOpen}>
                                <AccountCircle />
                            </IconButton>
                        ) : (
                            <IconButton
                                size="large"
                                edge="end"
                                onClick={handleLogin}>
                                <Login />
                            </IconButton>
                        )}
                        <ProfileMenu
                            anchorEl={profileMenuAnchor}
                            setAnchorEl={setProfileMenuAnchor}
                            setIsLoggedIn={setIsLoggedIn}
                        />
                    </Box>
                </Toolbar>
            </AppBar>
        </Box>
    );
}
