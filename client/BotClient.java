package com.javarush.task.task30.task3008.client;

import com.javarush.task.task30.task3008.ConsoleHelper;
import com.javarush.task.task30.task3008.Message;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class BotClient extends Client {

    public class BotSocketThread extends SocketThread {
        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {

            sendTextMessage("Привет чатику. Я бот. Понимаю команды: дата, день, месяц, год, время, час, минуты, секунды." );
            super.clientMainLoop();
        }

        
        protected void processIncomingMessage(String message) {
            super.processIncomingMessage(message);
            if (message==null || !message.contains(": ")) return;
            String[] data = message.split(": ");
            if (data.length!=2) return;
            String name = data[0];
            String text = data[1];
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = null;
            switch (text.trim()) {
                case "дата":
                    dateFormat = new SimpleDateFormat("dd.MM.YYYY");
                    break;
                case "день":
                    dateFormat = new SimpleDateFormat("d");
                    break;
                case "месяц":
                    dateFormat = new SimpleDateFormat("MMMM");
                    break;
                case "год":
                    dateFormat = new SimpleDateFormat("YYYY");
                    break;
                case "время":
                    dateFormat = new SimpleDateFormat("H:mm:ss");
                    break;
                case "час":
                    dateFormat = new SimpleDateFormat("H");
                    break;
                case "минуты":
                    dateFormat = new SimpleDateFormat("m");
                    break;
                case "секунды":
                    dateFormat = new SimpleDateFormat("s");
                    break;
            }
            if (dateFormat!=null) sendTextMessage(String.format("Информация для %s: %s",
                    name, dateFormat.format(calendar.getTime())));
        }
    }


    @Override
    protected SocketThread getSocketThread() {
        return new BotSocketThread();
    }

    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    protected String getUserName() {

        return "date_bot_" + (int) (Math.random() * 100);
    }


    public static void main(String[] args) throws Exception {
        BotClient botClient = new BotClient();
        botClient.run();
    }
}
