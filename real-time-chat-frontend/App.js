import './App.css';
import SockJs from 'sockjs-client'
import Stomp from 'stomp-websocket'
import {useEffect, useState} from "react";

function App() {

    const sock = new SockJs('http://localhost:8080/chat-ws');
    const stompClient = Stomp.over(sock);

    const [text, setText] = useState('')
    const [msgs, setMsgs] = useState([]);

    useEffect(() => {
        stompClient.debug = null
        stompClient.connect({}, () => {
            stompClient.subscribe('/topic/get', function (msg) {
                console.log('MSG', JSON.parse(msg.body));
                setMsgs(messages =>[...messages, JSON.parse(msg.body).message]);
            });
        });
    }, []);

    const sendMessage = () => {
        const messageObj = {
            "message": text,
            time: new Date(),
            fromUser: 0,
            toUser: 1
        }
        stompClient.send('/app/hello', {}, JSON.stringify(messageObj))
    }

    const onChange = (text) => {
        setText(text.target.value)
    }

    return <div>

        <label>
            Text:
            <input type="text" name="name" onChange={onChange}/>
        </label>
        <button onClick={sendMessage}>
            SendMsg
        </button>
        <ul>
        {msgs.map(function(data, index) {
            return <li key={ index }>{data}</li>;
        })}
        </ul>
    </div>;
}

export default App;
