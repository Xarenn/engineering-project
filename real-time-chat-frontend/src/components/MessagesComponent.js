import {Button, Chip, TextField} from "@mui/material";
import SockJs from "sockjs-client";
import Stomp from "stomp-websocket";
import {useEffect, useState} from "react";
import axios from "axios";
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";

const MessagesComponent = (messageObject) => {

    const sock = new SockJs('http://localhost:8080/rl-time-websocket');
    const stompClient = Stomp.over(sock);

    const [msgObjectId, setMsgObjectId] = useState();
    const [msgObject, setMsgObject] = useState();
    const [text, setText] = useState('')
    const [msgs, setMsgs] = useState([]);

    useEffect(() => {
        setMsgs([]);
        console.log("MSG COMPONENT", messageObject.messageObject);
        const msgData = messageObject.messageObject[0];
        if(msgData.length !== 0) {
            setMsgObjectId(msgData.messageObjectId);
            setMsgObject(msgData);
        }
        stompClient.debug = null
        stompClient.connect({}, () => {
            console.log("SUB FOR: ", '/topic/messages/get/'+msgData.messageObjectId)
            stompClient.subscribe('/topic/messages/get/'+msgData.messageObjectId, function (msg) {
                console.log('MSG ARRIVED WEBSOCKET', JSON.parse(msg.body));
                setMsgs(messages => [...messages, JSON.parse(msg.body)]);
            });
        });

        axios.get('/api/messages/data/'+msgData.messageObjectId+"?page=0",
            {headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}})
            .then(function (response) {
                console.log(response);
                console.log('messages', response.data);
                setMsgs(messages => [...messages, ...response.data.content]);
            })
            .catch(function (error) {
                console.log(error);
            });
    }, [messageObject]);

    const sendMessage = () => {
        const messageObj = {
            "message": text,
            time: new Date(),
            fromUser: Number(localStorage.getItem('id')),
            toUser: msgObject.toUser === Number(localStorage.getItem('id')) ? msgObject.fromUser : msgObject.toUser,
            fromUserName: msgObject.fromUser === Number(localStorage.getItem('id')) ? msgObject.fromUserName : msgObject.toUserName,
            toUserName: msgObject.fromUser === Number(localStorage.getItem('id')) ? msgObject.toUserName : msgObject.fromUserName,
            messageObjectId: msgObjectId
        }
        stompClient.send('/app/room/message/'+msgObjectId, {}, JSON.stringify(messageObj))
        //setMsgs(messages => [...messages, messageObj]);
    }

    const onChange = (text) => {
        setText(text.target.value)
    }

    const isMyMessage = (id) => {
        //console.log("ID", localStorage.getItem('id'), id, Number(localStorage.getItem('id')) === id);
        return Number(localStorage.getItem('id')) === id;
    }
    return (
        <Container style={{display: 'flex', flexDirection: 'column'}}>
            <div style={{display: 'flex', flexDirection: 'column'}}>
                {msgs.map(function(data, index) {
                    console.log("MSG", data);
                    return <Box style={{width: '100%', display: 'flex', flexDirection: isMyMessage(data.fromUser) ? 'row-reverse' : 'row', alignItems: 'flex-end'}} key={ index } >
                        <Box style={{display: 'flex', flexDirection: 'column', alignItems: isMyMessage(data.fromUser) ? 'flex-end' : 'flex-start'}}>
                            <h4 style={{color: 'gray', alignItems: 'flex-end', float: 'right'}}>{data.fromUserName}</h4>
                            <Box sx={{ borderRadius: '10%' }} style={{padding:'15px', alignItems: 'flex-start', textOverflow: 'overflow', maxWidth: '100px', backgroundColor: isMyMessage(data.fromUser) ? '#536186' : '#3261a8', color: 'white', fontWeight: '500'}}>
                                {data.message}
                            </Box>
                        </Box>
                    </Box>
                })}
            </div>
            <div style={{display: 'flex', flexDirection: 'row', marginTop: '20px', width: '100%'}}>
                <TextField id="filled-basic" label="Wiadomość" variant="filled" onChange={onChange}/>
                <Button variant="contained"  onClick={sendMessage}>Wyślij</Button>
            </div>
        </Container>
        );

};

export default MessagesComponent;