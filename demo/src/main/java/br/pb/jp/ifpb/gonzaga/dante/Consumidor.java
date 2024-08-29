package br.pb.jp.ifpb.gonzaga.dante;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Consumidor {
    public static void main(String[] args) throws Exception {
        System.out.println("Consumidor");

        String NOME_FILA = "tempoTotal";

        //criando a fabrica de conexoes e criando uma conexao
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("mqadmin");
        connectionFactory.setPassword("Admin123XX_"); 
        Connection conexao = connectionFactory.newConnection();

        //criando um canal e declarando uma fila
        Channel canal = conexao.createChannel();
        canal.queueDeclare(NOME_FILA, false, false, false, null);

        //Definindo a funcao callback
        DeliverCallback callback = (consumerTag, delivery) -> {
            String mensagem = new String(delivery.getBody());
            String[] mensagemDividida = mensagem.split("-");
            int numero = Integer.valueOf(mensagemDividida[0]);
            int tempo = Integer.valueOf(mensagemDividida[1]);
            if(numero == 1 || numero == 1000){
                System.out.println("Recebi a mensagem: " + mensagem);
                System.out.println("Tempo envio: " + tempo);
                System.out.println("Tempo recebimento: " + System.currentTimeMillis());
            }  
        };
        //Consome da fila
        canal.basicConsume(NOME_FILA, false, callback, consumerTag -> {});
        System.out.println("Continuarei executando outras atividades enquanto não chega mensagem...");
    }
}
