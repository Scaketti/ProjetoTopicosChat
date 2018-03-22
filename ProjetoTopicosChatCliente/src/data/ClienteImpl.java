/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import gui.TelaCliente;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * @author Scaketti
 */
public class ClienteImpl extends UnicastRemoteObject implements ClienteChatInterface{
    
    TelaCliente tCliente;
    Cliente cliente = new Cliente(null);
    
    public ClienteImpl(TelaCliente tCliente) throws RemoteException{
        super();
        this.tCliente = tCliente;
    }

    @Override
    public void receberMensagemServidor(String apelidoOrigem, String mensagem) throws RemoteException {
        //Procurar cliente na lista de clientes para poder salvar a mensagem no log de mensagens do objeto
        
        if(apelidoOrigem.equals("conectou") || apelidoOrigem.equals("desconectou")){
            
            tCliente.getTxtLogCliente().append("Você se " + apelidoOrigem + " ao servidor.\n");
        }
        else{
            tCliente.getTxtLogCliente().append("Cliente " + apelidoOrigem + " enviou uma mensagem.\n");
            tCliente.atualizaMensagemCliente(apelidoOrigem, mensagem);
        }
    }

    @Override
    public void receberNovaConexao(String apelido, String nome) throws RemoteException {
        tCliente.getTxtLogCliente().append("Cliente " + apelido + " se conectou.\n");
        tCliente.insereClienteLista(apelido);
    }

    @Override
    public void receberDesconexao(String apelido, String nome) throws RemoteException {
        tCliente.getTxtLogCliente().append("Cliente " + apelido + " se desconectou.\n");
        tCliente.removeClienteLista(apelido);
    }
    
}
