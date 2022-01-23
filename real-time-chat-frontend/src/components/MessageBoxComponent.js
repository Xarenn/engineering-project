import Container from "@mui/material/Container";
import SendIcon from '@mui/icons-material/Send';
import AutorenewIcon from '@mui/icons-material/Autorenew';
import {List, ListItem, ListItemButton, ListItemIcon, ListItemText, TextareaAutosize} from "@mui/material";
import {useEffect, useState} from "react";
import IconButton from "@mui/material/IconButton";
import {Badge} from "@mui/icons-material";
import axios from "axios";
import MessagesComponent from "./MessagesComponent";


const MessageBoxComponent = () => {

    const [objectMessages, setObjectMessages] = useState();
    const [selectedMessageObjectId, setSelectedMessageObjectId] = useState();
    const [objectMessagesPageInfo, setObjectMessagesPageInfo] = useState();

    useEffect(() => {
        axios.get('/api/messages/object?page=0', {headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}})
            .then(function (response) {
                console.log(response);
                console.log('data', response.data.content);
                setObjectMessages(response.data.content);
            })
            .catch(function (error) {
                console.log(error);
            });

    }, [])

    const handleRefresh = () => {
        console.log("REFERSH");
    }

    const getProperName = (msg) => {
        if(msg.fromUser !== localStorage.getItem('id')) {
            return msg.toUserName;
        }
        return msg.fromUserName;
    }

    const handleClickMessageObjectEvent = (event, messageObjectId) => {
        setSelectedMessageObjectId(messageObjectId);
    }
    console.log("XD", selectedMessageObjectId, !!selectedMessageObjectId);
    return (
        <Container style={{display: 'flex', flexDirection: 'row', marginTop: '50px'}}>
            <Container>
                    <IconButton onClick={handleRefresh}>
                        <AutorenewIcon/>
                    </IconButton>
                <List
                    sx={{
                        width: '100%',
                        maxWidth: 360,
                        bgcolor: 'background.paper',
                        position: 'relative',
                        overflow: 'auto',
                        maxHeight: 300,
                        '& ul': { padding: 0 },
                    }}>
                    {objectMessages?.map(msg =>
                        <ListItem disablePadding key={msg.messageObjectId}
                            selected={msg.messageObjectId === selectedMessageObjectId}
                                  onClick={(event) => handleClickMessageObjectEvent(event, msg.messageObjectId)}
                        >
                            <ListItemButton>
                                <ListItemText primary={getProperName(msg)} secondary={msg.lastMessage} />
                            </ListItemButton>
                            <ListItemIcon>
                                <SendIcon />
                            </ListItemIcon>
                        </ListItem>
                    )}
                </List>
            </Container>
            <Container style={{}}>
                {!!selectedMessageObjectId ? <MessagesComponent messageObject={objectMessages?.filter(msg => msg.messageObjectId === selectedMessageObjectId)}/> : <div/>}
                {/*<TextareaAutosize*/}
                {/*    aria-label="wiadomosc"*/}
                {/*    minRows={3}*/}
                {/*    placeholder="Napisz wiadomość"*/}
                {/*    style={{ minWidth: 400, minHeight: 100 }}*/}
                {/*/>*/}
            </Container>
            <Container>
                <h1>Kafka Statystyki</h1>
                <List>
                    <ListItem disablePadding>
                        <ListItemButton>
                            <ListItemText primary="reply-messages" />
                        </ListItemButton>
                        <ListItemText edge="end" secondary="12.2212313" />
                    </ListItem>
                    <ListItem disablePadding>
                        <ListItemButton>
                            <ListItemText primary="reply-data" />
                        </ListItemButton>
                        <ListItemText edge="end" secondary="12.2212313" />
                    </ListItem>
                </List>
            </Container>
        </Container>
    );

};

export default MessageBoxComponent;