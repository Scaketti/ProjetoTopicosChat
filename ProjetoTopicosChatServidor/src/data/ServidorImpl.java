/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import gui.TelaServidor;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import sun.rmi.registry.RegistryImpl;

/**
 *
 * @author Scaketti
 */
public class ServidorImpl extends UnicastRemoteObject implements ServidorChatInterface {

    ArrayList<Cliente> clientesConectados = new ArrayList();
    TelaServidor tServidor;

    public ServidorImpl(TelaServidor tela) throws RemoteException {
        super();
        this.tServidor = tela;
    }

    @Override
    public int receberMensagemCliente(String apelidoOrigem, String apelidoDestino, String mensagem) throws RemoteException {
        Cliente clienteDestino = null;
        for (Cliente c : clientesConectados) {
            if (c.getApelido().equals(apelidoDestino)) {
                clienteDestino = c;
                break;
            }
        }

        if (clienteDestino != null) {
            try {
                ClienteChatInterface cliente = (ClienteChatInterface) Naming.lookup("rmi://" + clienteDestino.getIp() + ":" + clienteDestino.getPorta() + "/chat");
                cliente.receberMensagemServidor(apelidoOrigem, mensagem);
                tServidor.getTxtLog().append("Cliente " + apelidoOrigem + " mandou uma mensagem para " + apelidoDestino + ".\n");

            } catch (Exception e) {
                System.out.println("Erro: Mensagem: " + e.getMessage());
            }
        }
        return 1;
    }

    @Override
    public int conectar(String apelido, String nome, String ipCliente, String portaCliente) throws RemoteException {
        Boolean valido = false;

        //Verifica se possui algum cliente conectado com o mesmo apelido
        for (Cliente c : clientesConectados) {
            if (c == null) {
                break;
            }
            valido = verificaApelido(apelido, c);
            if (valido) {
                break;
            }
        }

        //Caso o apelido for válido, será efetuado a conexão com o servidor
        if (!valido) {
            try {
                ClienteChatInterface cliente = (ClienteChatInterface) Naming.lookup("rmi://" + ipCliente + ":" + portaCliente + "/chat");
                cliente.receberMensagemServidor("conectou", "");
            } catch (Exception e) {
                System.out.println("Erro: Mensagem: " + e.getMessage());
            }

            Cliente clienteConectando = new Cliente(apelido, nome, ipCliente, portaCliente);

            for (Cliente c : clientesConectados) {
                notificarConexao(clienteConectando, c);
            }

            clientesConectados.add(clienteConectando);

            tServidor.insereClienteLista(clienteConectando);

            tServidor.getTxtLog().append("Cliente " + clienteConectando.getApelido() + " se conectou ao servidor.\n");

            return 0;
        }

        return 1;
    }

    @Override
    public void desconectar(String apelido, String ipCliente, String portaCliente) throws RemoteException {
        Boolean valido = false;

        //Verifica se o mesmo ja está conectado
        for (Cliente c : clientesConectados) {
            if (c == null) {
                break;
            }
            valido = verificaApelido(apelido, c);
            if (valido) {
                clientesConectados.remove(c);
                tServidor.removeClienteLista(c);
                break;
            }
        }

        if (valido) {
            try {
                ClienteChatInterface cliente = (ClienteChatInterface) Naming.lookup("rmi://" + ipCliente + ":" + portaCliente + "/chat");
                cliente.receberMensagemServidor("desconectou", "");
            } catch (Exception e) {
                System.out.println("Erro: Mensagem: " + e.getMessage());
            }

            for (Cliente c : clientesConectados) {
                notificarDesconexao(c);
            }

            tServidor.getTxtLog().append("Cliente " + apelido + " se desconectou ao servidor.\n");
        }
    }

    private void notificarConexao(Cliente conectado, Cliente c) {
        String mensagem = "Cliente: " + conectado.getApelido() + "se conectou ao servidor.";
        try {
            ClienteChatInterface cliente = (ClienteChatInterface) Naming.lookup("rmi://" + c.getIp() + ":" + c.getPorta() + "/chat");
            cliente.receberNovaConexao(conectado.getApelido(), mensagem);
        } catch (Exception e) {

        }
        //envia uma mensagem aos clientes conectados avisando que alguêm foi conectado
    }

    private void notificarDesconexao(Cliente c) {
        String mensagem = "Cliente: " + c.getApelido() + "se desconectou do servidor.";
        //envia uma mensagem aos clientes conectados avisando que alguêm foi desconectado
    }

    private Boolean verificaApelido(String apelido, Cliente c) {
        return c.getApelido().equals(apelido);
    }

}
