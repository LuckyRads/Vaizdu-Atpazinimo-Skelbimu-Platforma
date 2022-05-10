import { styled, Typography } from '@mui/material';

// eslint-disable-next-line import/prefer-default-export
export const TruncatedTypography = styled(Typography)`
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    display: block;
`;
