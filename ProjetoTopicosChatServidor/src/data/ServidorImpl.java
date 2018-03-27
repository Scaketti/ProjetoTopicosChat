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

/**
 *
 * @author Scaketti
 */
public class ServidorImpl extends UnicastRemoteObject implements ServidorChatInterface {

    ArrayList<Cliente> clientesConectados = new ArrayList();
    TelaServidor tServidor;
    ClienteChatInterface cliente;

    public ServidorImpl(TelaServidor tela) throws RemoteException {
        super();
        this.tServidor = tela;
    }

    @Override
    public int receberMensagemCliente(String apelidoOrigem, String apelidoDestino, String mensagem) throws RemoteException {
        Cliente clienteDestino = null;

        if (apelidoDestino.equals("TODOS")) { //Mensagem em broadcast
            try {
                for (Cliente c : clientesConectados) {
                    cliente = (ClienteChatInterface) Naming.lookup("rmi://" + c.getIp() + ":" + c.getPorta() + "/chat");
                    cliente.receberMensagemServidor(apelidoOrigem, mensagem);
                }
            } catch (Exception e) {
                System.out.println("Erro: Mensagem: " + e.getMessage());
                return 1;
            }
            tServidor.getTxtLog().append("Cliente " + apelidoOrigem + " mandou uma mensagem para todos.\n");
            return 0;
        } else { //Mensagem para um determinado cliente
            for (Cliente c : clientesConectados) { //Procura o cliente na lista
                if (c.getApelido().equals(apelidoDestino)) {
                    clienteDestino = c;
                    break;
                }
            }

            if (clienteDestino != null) { //Caso o cliente esteja na lista, tenta mandar a mensagem
                try {
                    cliente = (ClienteChatInterface) Naming.lookup("rmi://" + clienteDestino.getIp() + ":" + clienteDestino.getPorta() + "/chat");
                    cliente.receberMensagemServidor(apelidoOrigem, mensagem);
                    tServidor.getTxtLog().append("Cliente " + apelidoOrigem + " mandou uma mensagem para " + apelidoDestino + ".\n");
                    return 0;
                } catch (Exception e) {
                    System.out.println("Erro: Mensagem: " + e.getMessage());
                    return 1;
                }
            }
        }
        return 1;
    }

    @Override
    public int conectar(String apelido, String nome, String ipCliente, String portaCliente) throws RemoteException {
        Boolean valido = false;

        //Verifica se possui algum cliente conectado com o mesmo apelido
        for (Cliente c : clientesConectados) {
            valido = verificaApelido(apelido, c);
            if (valido) {
                return 1;
            }
        }

        if (!valido) { //Caso não exista nenhum cliente com o mesmo nome, tenta conectar
            Cliente clienteConectando = new Cliente(apelido, nome, ipCliente, portaCliente);
            clientesConectados.add(clienteConectando);
            tServidor.insereClienteLista(clienteConectando); //Chama método que insere o cliente na lista da tela
            
            for (Cliente cl : clientesConectados) { //Notifica os clientes 
                if (!cl.getApelido().equals(apelido)) {
                    notificarConexao(clienteConectando, cl); //Notifica a pessoa que ja está conectada
                    notificarConexao(cl, clienteConectando); //Recebe "ping" da pessoa que ja está conectada
                }
            }
            return 0;
        }
        return 1;
    }

    @Override
    public void desconectar(String apelido, String ipCliente, String portaCliente) throws RemoteException {
        Boolean valido = false;

        //Verifica se o mesmo ja está conectado
        for (Cliente c : clientesConectados) {
            valido = verificaApelido(apelido, c);
            if (valido) { //Caso ele esteja conectado, notifica a desconexão
                for (Cliente cl : clientesConectados) {
                    if (!cl.getApelido().equals(apelido)) {
                        notificarDesconexao(apelido, cl);
                    }
                }
                clientesConectados.remove(c);
                tServidor.removeClienteLista(c);
                break;
            }
        }
    }

    private void notificarConexao(Cliente conectando, Cliente c) {
        try { //Envia uma mensagem ao cliente conectado avisando que alguêm se conectou
            cliente = (ClienteChatInterface) Naming.lookup("rmi://" + c.getIp() + ":" + c.getPorta() + "/chat");
            cliente.receberNovaConexao(conectando.getApelido(), conectando.getNome());
        } catch (Exception e) {
            System.out.println("Teste Erro: Mensagem: " + e.getMessage());
        }
    }

    private void notificarDesconexao(String apelido, Cliente c) {
        String mensagem = "Cliente: " + apelido + "se desconectou do servidor.";
        try { //Envia uma mensagem ao cliente conectado avisando que alguêm se desconectou
            cliente = (ClienteChatInterface) Naming.lookup("rmi://" + c.getIp() + ":" + c.getPorta() + "/chat");
            cliente.receberDesconexao(apelido, mensagem);
        } catch (Exception e) {
            System.out.println("Erro: Mensagem: " + e.getMessage());
        }
    }

    private Boolean verificaApelido(String apelido, Cliente c) {
        if (c == null) {
            return false;
        }
        return c.getApelido().equals(apelido);
    }

}
