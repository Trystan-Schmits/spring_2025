package com.nighthawk.spring_portfolio.mvc.mortevision;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.onvoid.webrtc.CreateSessionDescriptionObserver;
import dev.onvoid.webrtc.PeerConnectionFactory;
import dev.onvoid.webrtc.PeerConnectionObserver;
import dev.onvoid.webrtc.RTCAnswerOptions;
import dev.onvoid.webrtc.RTCConfiguration;
import dev.onvoid.webrtc.RTCIceCandidate;
import dev.onvoid.webrtc.RTCIceServer;
import dev.onvoid.webrtc.RTCPeerConnection;
import dev.onvoid.webrtc.RTCRtpReceiver;
import dev.onvoid.webrtc.RTCSdpType;
import dev.onvoid.webrtc.RTCSessionDescription;
import dev.onvoid.webrtc.SetSessionDescriptionObserver;
import dev.onvoid.webrtc.media.MediaStream;
import dev.onvoid.webrtc.media.MediaStreamTrack;

@RestController
@RequestMapping("/webrtc")
public class SFU implements PeerConnectionObserver {

    MediaStreamTrack videoTrack;
    MediaStreamTrack audioTrack;

    @PostMapping("/consume")
    public Map<String,String> consumer(@RequestBody String body) {
        if (videoTrack == null) {
            HashMap<String, String> map = new HashMap<>();
            map.put("error","no broadcast");
            return map;
        }
        String sdp = new JSONObject(body).getJSONObject("sdp").getString("sdp");
        RTCIceServer iceServer = new RTCIceServer();
        iceServer.urls = Collections.singletonList("stun:stun.l.google.com:19302");

        RTCConfiguration config = new RTCConfiguration();
        config.iceServers = Collections.singletonList(iceServer);

        PeerConnectionFactory PCF = new PeerConnectionFactory();
        RTCPeerConnection peerConnection = PCF.createPeerConnection(config, this);

        RTCSessionDescription sessionDescription = new RTCSessionDescription(RTCSdpType.OFFER, sdp);
        peerConnection.setRemoteDescription(sessionDescription, new SetSessionDescriptionObserver() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                // throw new UnsupportedOperationException("Unimplemented method 'onSuccess'");
            }

            @Override
            public void onFailure(String error) {
                // TODO Auto-generated method stub
                // throw new UnsupportedOperationException("Unimplemented method 'onFailure'");
            }
            
        }); // this is most likley the problem

        if(videoTrack != null)
        {
            peerConnection.addTrack(videoTrack, Collections.singletonList(videoTrack.getId()));
        }

        if(audioTrack != null)
        {
            peerConnection.addTrack(audioTrack, Collections.singletonList(audioTrack.getId()));
        }
        
        
        peerConnection.createAnswer(new RTCAnswerOptions(), new CreateSessionDescriptionObserver() {

            @Override
            public void onSuccess(RTCSessionDescription description) {
                peerConnection.setLocalDescription(description, new SetSessionDescriptionObserver() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        // throw new UnsupportedOperationException("Unimplemented method 'onSuccess'");
                    }
        
                    @Override
                    public void onFailure(String error) {
                        // TODO Auto-generated method stub
                        // throw new UnsupportedOperationException("Unimplemented method 'onFailure'");
                    }
                    
                });
            }

            @Override
            public void onFailure(String error) {
                // TODO Auto-generated method stub
                // throw new UnsupportedOperationException("Unimplemented method 'onFailure'");
            }
            
        });
     
        HashMap<String, String> map = new HashMap<>();
        map.put("type",peerConnection.getLocalDescription().sdpType.name().toLowerCase());
        map.put("sdp",peerConnection.getLocalDescription().sdp);
        return map;
    }

    @PostMapping(value="/broadcast",consumes ="application/json")
    public Map<String, String> broadcast(@RequestBody String body) {
        String sdp = new JSONObject(body).getJSONObject("sdp").getString("sdp");
        RTCIceServer iceServer = new RTCIceServer();
        iceServer.urls = Collections.singletonList("stun:stun.l.google.com:19302");
        RTCConfiguration config = new RTCConfiguration();
        config.iceServers = Collections.singletonList(iceServer);
        PeerConnectionFactory PCF = new PeerConnectionFactory();
        RTCPeerConnection peerConnection = PCF.createPeerConnection(config, this);
        RTCSessionDescription sessionDescription = new RTCSessionDescription(RTCSdpType.OFFER, sdp);
        peerConnection.setRemoteDescription(sessionDescription, new SetSessionDescriptionObserver() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                // throw new UnsupportedOperationException("Unimplemented method 'onSuccess'");
            }

            @Override
            public void onFailure(String error) {
                // TODO Auto-generated method stub
                // throw new UnsupportedOperationException("Unimplemented method 'onFailure'");
            }
            
        });

        peerConnection.createAnswer(new RTCAnswerOptions(), new CreateSessionDescriptionObserver() {

            @Override
            public void onSuccess(RTCSessionDescription description) {
                peerConnection.setLocalDescription(description, new SetSessionDescriptionObserver() {

                    @Override
                    public void onSuccess() {
                        // throw new UnsupportedOperationException("Unimplemented method 'onSuccess'");
                    }

                    @Override
                    public void onFailure(String error) {
                        // throw new UnsupportedOperationException("Unimplemented method 'onFailure'");
                    }
                    
                });
            }

            @Override
            public void onFailure(String error) {
                // throw new UnsupportedOperationException("Unimplemented method 'onFailure'");
            }

        });
        HashMap<String, String> map = new HashMap<>();
        map.put("type",peerConnection.getLocalDescription().sdpType.name().toLowerCase());
        map.put("sdp",peerConnection.getLocalDescription().sdp);
        return map;
    }

    private final Object lock = new Object();

    @Override
    public void onAddTrack(RTCRtpReceiver receiver, MediaStream[] mediaStreams) {
        synchronized (lock) {
        System.out.println("track add");
        if(receiver.getTrack().getKind().equals("video"))
        {
            videoTrack = receiver.getTrack();
        }
        /* 
        else if(receiver.getTrack().getKind().equals("audio"))
        {
            audioTrack = receiver.getTrack();
        }
            */
    }
    }

    @Override
    public void onIceCandidate(RTCIceCandidate candidate) {
    }
}