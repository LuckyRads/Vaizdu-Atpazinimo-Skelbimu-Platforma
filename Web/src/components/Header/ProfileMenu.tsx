import { Menu, MenuItem } from '@mui/material';
import React, { Dispatch, ReactElement, SetStateAction } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthService from '../../authorization/AuthService';

type PropsType = {
    anchorEl: HTMLElement | null;
    setAnchorEl: Dispatch<SetStateAction<HTMLElement | null>>;
    setIsLoggedIn: Dispatch<SetStateAction<boolean | null>>;
};

export default function ProfileMenu(props: PropsType): ReactElement {
    const navigate = useNavigate();
    const isMenuOpen = Boolean(props.anchorEl);

    const handleMenuClose = (): void => {
        props.setAnchorEl(null);
    };

    const handleOpenOwnedItems = (): void => {
        props.setAnchorEl(null);
        navigate('/owned-items');
    };

    const handleLogout = (): void => {
        props.setAnchorEl(null);
        props.setIsLoggedIn(false);
        AuthService.logout();
    };

    return (
        <Menu
            anchorEl={props.anchorEl}
            anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
            keepMounted
            transformOrigin={{ vertical: 'top', horizontal: 'right' }}
            open={isMenuOpen}
            onClose={handleMenuClose}>
            <MenuItem onClick={handleOpenOwnedItems}>Owned items</MenuItem>
            <MenuItem onClick={handleLogout}>Logout</MenuItem>
        </Menu>
    );
}
