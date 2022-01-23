import {Button, Chip} from "@mui/material";
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
    const [text, setText] = useState('')
    const [msgs, setMsgs] = useState([]);

    useEffect(() => {
        setMsgs([]);
        console.log("MSG COMPONENT", messageObject.messageObject);
        const msgData = messageObject.messageObject[0];
        if(msgData.length !== 0) {
            setMsgObjectId(msgData.messageObjectId);
        }
        stompClient.debug = null
        stompClient.connect({}, () => {
            console.log("SUB FOR: ", '/topic/messages/get/'+msgData.messageObjectId)
            stompClient.subscribe('/topic/messages/get/'+msgData.messageObjectId, function (msg) {
                console.log('MSG', JSON.parse(msg.body));
                setMsgs(messages =>[...messages, JSON.parse(msg.body).message]);
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
            fromUser: 1,
            toUser: 2,
            messageObjectId: 3
        }
        stompClient.send('/app/room/message/'+msgObjectId, {}, JSON.stringify(messageObj))
    }

    const onChange = (text) => {
        setText(text.target.value)
    }

    const isMyMessage = (id) => {
        console.log("ID", localStorage.getItem('id'), id);
        return Number(localStorage.getItem('id')) === Number(id);
    }

    return (
        <Container>
            <div style={{display: 'flex', flexDirection: 'column'}}>
                {msgs.map(function(data, index) {
                    console.log("MSG", data);
                    console.log("WTF", isMyMessage(data.fromUser));
                    return <Box style={{display: 'flex', flexDirection: isMyMessage(data.fromUser) ? 'row-reverse' : 'row', alignItems: 'flex-end'}}>
                        <Chip style={{maxWidth: '100px', backgroundColor: isMyMessage(data.fromUser) ? '#32a852' : '#32a2a8'}} key={ index } label={data.message}/>
                    </Box>
                })}
            </div>
            <label>
                Text:
                <input type="text" name="name" onChange={onChange}/>
            </label>
            <Button variant="contained"  onClick={sendMessage}>SendMsg</Button>
        </Container>
        );

};

export default MessagesComponent;