package mp9.uf3.udp.multicast.exemple;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class ClienteMulticast {

    private boolean continueRunning = true;
    private MulticastSocket socket;
    private InetAddress multicastIP;
    private int port;
    private Map<String, Integer> paraulesMap;

    public ClienteMulticast (int portValue, String strIp) throws IOException {
        multicastIP = InetAddress.getByName(strIp);
        port = portValue;
        socket = new MulticastSocket(port);
        paraulesMap = new HashMap<>();
    }

    public void runClient() throws IOException {
        DatagramPacket packet;
        byte[] receivedData = new byte[1024];

        socket.joinGroup(new InetSocketAddress(multicastIP, port), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
        System.out.printf("Connectat a %s:%d%n", multicastIP, port);

        while (continueRunning) {
            // Recibe un paquete con la palabra enviada por el servidor
            packet = new DatagramPacket(receivedData, receivedData.length);
            socket.receive(packet);

            // Procesa los datos recibidos (actualiza el mapa de palabras y su frecuencia)
            continueRunning = processarDades(packet.getData());
        }

        socket.leaveGroup(multicastIP);
        socket.close();
    }

    protected boolean processarDades(byte[] data) {
        String paraula = new String(data).trim();
        paraulesMap.put(paraula, paraulesMap.getOrDefault(paraula, 0) + 1);

        // Muestra la palabra actual y la cantidad de veces que se ha mostrado
        System.out.println("Paraula actual: " + paraula + ", s'ha mostrat " + paraulesMap.get(paraula) + " vegades");

        return true;
    }

    public static void main(String[] args) throws IOException {
        ClienteMulticast clientParaules = new ClienteMulticast(5558, "224.0.11.112");
        clientParaules.runClient();
        System.out.println("Parat!");
    }
}
