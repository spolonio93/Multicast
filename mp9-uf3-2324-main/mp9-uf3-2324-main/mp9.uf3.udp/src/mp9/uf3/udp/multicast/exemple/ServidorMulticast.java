package mp9.uf3.udp.multicast.exemple;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServidorMulticast {

    private MulticastSocket socket;
    private InetAddress multicastIP;
    private int port;
    private boolean continueRunning = true;
    private List<String> paraules;

    // Este es el constructor que inicializa el servidor multicast con la dirección IP y el puerto
    public ServidorMulticast (int portValue, String strIp) throws IOException {
        socket = new MulticastSocket(portValue);
        multicastIP = InetAddress.getByName(strIp);
        port = portValue;
        paraules = generarLlistaParaules();
    }

    // Generamos una lista de palabras predefinidas
    private List<String> generarLlistaParaules() {
        List<String> paraules = new ArrayList<>();
        paraules.add("gat");
        paraules.add("cotxe");
        paraules.add("arbre");
        paraules.add("lluna");
        paraules.add("sol");
        return paraules;
    }

    public void runServer() throws IOException {
        DatagramPacket packet;
        byte[] sendingData;

        // Obtiene una palabra aleatoria, la convierte en bytes y la envía como un paquete Datagrama al grupo multicast
        while (continueRunning) {
            String paraula = obtenirParaulaAleatoria();
            sendingData = paraula.getBytes();
            packet = new DatagramPacket(sendingData, sendingData.length, multicastIP, port);
            socket.send(packet);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.getMessage();
            }
        }
        socket.close();
    }

    private String obtenirParaulaAleatoria() {
        Random random = new Random();
        int index = random.nextInt(paraules.size());
        return paraules.get(index);
    }

    public static void main(String[] args) throws IOException {
        ServidorMulticast srvParaules = new ServidorMulticast(5558, "224.0.11.112");
        srvParaules.runServer();
        System.out.println("Parat!");
    }
}
