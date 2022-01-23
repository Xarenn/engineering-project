import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import IconButton from "@mui/material/IconButton";
import MarkEmailUnreadIcon from '@mui/icons-material/MarkEmailUnread';
import {Autocomplete} from "@mui/material";
import {useEffect} from "react";
import axios from "axios";
import {reloadPageWithToken} from "./LoginComponent";
import {useNavigate} from "react-router-dom";

const FormDialog = (objectMessages) => {
    const navigate = useNavigate();
    const [open, setOpen] = React.useState(false);
    const [users, setUsers] = React.useState([]);
    const [selectedUser, setSelectedUser] = React.useState([]);

    useEffect(() => {
        axios.get('/api/user/list')
            .then(function (response) {
                console.log(response);
                console.log('users, msgs', response.data, objectMessages.objectMessages);
                const idsFromInitialized = objectMessages.objectMessages.map(msg => msg.fromUser);
                idsFromInitialized.push(Number(localStorage.getItem('id')));
                const idsToInitialized = objectMessages.objectMessages.map(msg => msg.toUser);
                const idsInitialized = new Set([...idsFromInitialized, ...idsToInitialized]);
                console.log("IDS INITIALIZED", idsInitialized);
                const usersFiltered = response.data.filter(usr => !idsInitialized.has(usr.id))
                console.log("USERS FILTERED", usersFiltered);
                setUsers(usersFiltered);
            })
            .catch(function (error) {
                console.log(error);
            });
    }, [objectMessages])

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleUserChange = (user) => {
        setSelectedUser(user);
        console.log("SELECTED USR", user);
    }

    const handleSendMessage = () => {
        const data = {
            fromUser: localStorage.getItem('id'),
            toUser: selectedUser.id,
            toUserName: selectedUser.name,
            lastMessage: 'Inicjuje chat'
        };
        axios.post('/api/messages/new/add', data, {
            headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`},
        })
            .then(function (response) {
                console.log(response);
                console.log('INICJALIZACJA WIADOMOSCI', response.data);
                window.location.reload();
            })
            .catch(function (error) {
                console.log(error);
                if (error?.message?.includes("JWT expired") || error?.errorMessage?.includes("JWT expired")) {
                    localStorage.clear();
                    window.location.reload();
                    reloadPageWithToken(navigate);
                }
            });
        setOpen(false);
    }

    return (
        <div>
            <IconButton>
                <MarkEmailUnreadIcon onClick={handleClickOpen}/>
            </IconButton>
            <Dialog open={open} onClose={handleClose} style={{minHeight: '80vh', height: '450px'}}>
                <DialogTitle>Subscribe</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Napisz wiadomosc do uzytkownika - wyszukaj go tutaj w naszej bazie
                    </DialogContentText>
                    <Autocomplete
                        style={{marginTop: '50px'}}
                        disablePortal
                        id="autocomplete-user"
                        options={users}
                        onChange={(event, value) => handleUserChange(value)}
                        sx={{width: 500}}
                        renderInput={(params) => <TextField {...params} label="Nazwa lub Imie"/>}
                    />
                </DialogContent>
                <DialogActions style={{marginTop: '100px'}}>
                    <Button onClick={handleClose}>Anuluj</Button>
                    <Button onClick={handleSendMessage}>Wyslij</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}

export default FormDialog;