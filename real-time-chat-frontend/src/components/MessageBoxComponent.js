import Container from "@mui/material/Container";
import SendIcon from '@mui/icons-material/Send';
import AutorenewIcon from '@mui/icons-material/Autorenew';
import {List, ListItem, ListItemButton, ListItemIcon, ListItemText, TextareaAutosize} from "@mui/material";
import {useEffect, useState} from "react";
import IconButton from "@mui/material/IconButton";
import axios from "axios";
import MessagesComponent from "./MessagesComponent";
import {reloadPageWithToken} from "./LoginComponent";
import {useNavigate} from "react-router-dom";
import FormDialog from "./CreateNewMessageDialogComponent";
import Box from "@mui/material/Box";


const MessageBoxComponent = () => {

    const navigate = useNavigate();
    const [objectMessages, setObjectMessages] = useState();
    const [kafkaStatistics, setKafkaStatistics] = useState();
    const [kafkaConsumer, setKafkaConsumer] = useState();
    const [selectedMessageObjectId, setSelectedMessageObjectId] = useState();

    const mapEntry = (entry) => {
        return {
            key: entry[0],
            value: entry[1]
        }
    }

    useEffect(() => {
        axios.get('/api/kafka/statistics', {headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}})
            .then(function (response) {
                console.log(response);
                console.log('KAFKA STATISTICS', response.data);
                setKafkaStatistics(Object.entries(response.data[0].metrics).map(entry => mapEntry(entry)));
                setKafkaConsumer(response.data[0].consumerName);
                console.log('KAFKA METRICS', Object.entries(response.data[0].metrics).map(entry => mapEntry(entry)));
            })
            .catch(function (error) {
                console.log(error);
                if(error?.message?.includes("JWT expired") || error?.errorMessage?.includes("JWT expired")) {
                    localStorage.clear();
                    window.location.reload();
                    reloadPageWithToken(navigate);
                }
            });

        axios.get('/api/messages/object?page=0', {headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}})
            .then(function (response) {
                console.log(response);
                console.log('data', response.data.content);
                setObjectMessages(response.data.content);
            })
            .catch(function (error) {
                console.log(error);
                if(error?.message?.includes("JWT expired") || error?.errorMessage?.includes("JWT expired")) {
                    localStorage.clear();
                    window.location.reload();
                    reloadPageWithToken(navigate);
                }
            });
    }, [])

    const handleRefresh = () => {
        console.log("REFERSH");
    }

    const getProperName = (msg) => {
        if(msg.fromUser === Number(localStorage.getItem('id'))) {
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
                <h3>{localStorage.getItem('name')}</h3>
                <Box style={{display: 'flex', flexDirection: 'row-reverse'}}>
                    <IconButton onClick={handleRefresh}>
                        <AutorenewIcon/>
                    </IconButton>
                    <FormDialog objectMessages={objectMessages}/>
                </Box>
                <List
                    sx={{
                        width: '100%',
                        maxWidth: 360,
                        bgcolor: 'background.paper',
                        position: 'relative',
                        overflow: 'auto',
                        maxHeight: 1200,
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
                {!!selectedMessageObjectId ?
                    <MessagesComponent messageObject={objectMessages?.filter(msg => msg.messageObjectId === selectedMessageObjectId)}/>
                    : <div/>}
                {/*<TextareaAutosize*/}
                {/*    aria-label="wiadomosc"*/}
                {/*    minRows={3}*/}
                {/*    placeholder="Napisz wiadomość"*/}
                {/*    style={{ minWidth: 400, minHeight: 100 }}*/}
                {/*/>*/}
            </Container>
            <Container>
                <h2>Kafka {kafkaConsumer}</h2>
                <List>
                    {kafkaStatistics?.map(statistic => {
                        return <ListItem disablePadding>
                            <ListItemButton>
                                <ListItemText primary={statistic.key.split(":")[1]} />
                            </ListItemButton>
                            <ListItemText edge="end" secondary={statistic.value} />
                        </ListItem>
                    })}
                </List>
            </Container>
        </Container>
    );

};

export default MessageBoxComponent;