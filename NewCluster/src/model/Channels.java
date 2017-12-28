package model;
import java.util.ArrayList;
import java.util.HashSet;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "Channels")
public class Channels {
    @XmlElement(name = "Channel")
    public HashSet<Channel> listChannel = new HashSet<>();
}
