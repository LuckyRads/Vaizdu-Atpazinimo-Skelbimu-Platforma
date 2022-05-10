import { ThemeProvider } from '@mui/material';
import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import Header from '../components/Header/Header';
import useLoader from '../hooks/useLoader';
import Home from '../pages/Home';
import ItemModification from '../pages/ItemModification';
import SignIn from '../pages/SignIn';
import SignUp from '../pages/SignUp';
import theme from '../providers/theme';
import './App.css';

function App(): React.ReactElement {
    const [loader, showLoader, hideLoader] = useLoader();

    return (
        <ThemeProvider theme={theme}>
            <ToastContainer
                position="top-right"
                autoClose={5000}
                hideProgressBar={false}
                pauseOnHover
                newestOnTop
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
            />
            {loader}
            <BrowserRouter>
                <Header />
                <Routes>
                    <Route
                        path="/sign-in"
                        element={
                            <SignIn
                                showLoader={showLoader}
                                hideLoader={hideLoader}
                            />
                        }
                    />
                    <Route
                        path="/sign-up"
                        element={
                            <SignUp
                                showLoader={showLoader}
                                hideLoader={hideLoader}
                            />
                        }
                    />
                    <Route
                        path="/home"
                        element={
                            <Home
                                showLoader={showLoader}
                                hideLoader={hideLoader}
                                isOwnedItems={false}
                            />
                        }
                    />
                    <Route
                        path="/item/:id"
                        element={
                            <ItemModification
                                showLoader={showLoader}
                                hideLoader={hideLoader}
                            />
                        }
                    />
                    <Route
                        path="/item"
                        element={
                            <ItemModification
                                showLoader={showLoader}
                                hideLoader={hideLoader}
                            />
                        }
                    />
                    <Route
                        path="/owned-items"
                        element={
                            <Home
                                showLoader={showLoader}
                                hideLoader={hideLoader}
                                isOwnedItems
                            />
                        }
                    />
                    <Route
                        path="*"
                        element={
                            <Home
                                showLoader={showLoader}
                                hideLoader={hideLoader}
                                isOwnedItems={false}
                            />
                        }
                    />
                </Routes>
            </BrowserRouter>
        </ThemeProvider>
    );
}

export default App;
