import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

class CashRegister {
    private final int id;
    private final ReentrantLock lock = new ReentrantLock();

    public CashRegister(int id) {
        this.id = id;
    }

    public boolean tryUse(int customerId) {
        if (lock.tryLock()) {//ожидаем блокировки
            try {
                System.out.println("Клиент " + customerId + " обслуживается на кассе " + id +
                        " в потоке " );
                Thread.sleep(new Random().nextInt(2000) + 1000);
                System.out.println("Клиент " + customerId + " завершил обслуживание на кассе " + id);
                return true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
        return false;
    }
}

class Customer extends Thread {
    private final int id;
    private final List<CashRegister> cashRegisters;
    private final Semaphore semaphore;

    public Customer(int id, List<CashRegister> cashRegisters, Semaphore semaphore) {
        this.id = id;
        this.cashRegisters = cashRegisters;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();//ограничиваем колво потоков к кассам
            for (CashRegister cashRegister : cashRegisters) {
                if (cashRegister.tryUse(id)) {
                    break;//чтоб дальше не лез и не искал кассы
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();//освобождаем место под след потоки
        }
    }
}

public class Work {
    public static void main(String[] args) {
        int numCashRegisters = 5;
        int numCustomers = 10;

        List<CashRegister> cashRegisters = new ArrayList<>();
        for (int i = 0; i < numCashRegisters; i++) {
            cashRegisters.add(new CashRegister(i + 1));
        }

        Semaphore semaphore = new Semaphore(numCashRegisters);

        List<Thread> customerThreads = new ArrayList<>();
        for (int i = 0; i < numCustomers; i++) {
            Thread customer = new Customer(i + 1, cashRegisters, semaphore);
            customerThreads.add(customer);
            customer.start();
        }

        for (Thread customer : customerThreads) {
            try {
                customer.join();//ожидаем пока клиенты выйдут с покупками
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}