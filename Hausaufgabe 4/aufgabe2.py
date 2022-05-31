from client import Client
from threading import Thread

client1 = Client(10, 19)
client2 = Client(20, 29)
client3 = Client(30, 39)


# running the client transactions
thread1 = Thread(target = client1.execute_transactions)
thread2 = Thread(target = client2.execute_transactions)
thread3 = Thread(target = client3.execute_transactions)
thread1.start()
thread2.start()
thread3.start()
