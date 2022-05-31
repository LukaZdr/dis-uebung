from persistance_manager import PersistanceManager
import time

pm = PersistanceManager()
pm2 = PersistanceManager()

print('Same Object: ', id(pm) == id(pm2))

# start a transaction

# write user data
transaction_id = pm.begin_transaction()
pm.write(transaction_id, 1, 'Tom - 21 - Hamburg')
pm.write(transaction_id, 2, 'Tim - 25 - Berlin')
pm.write(transaction_id, 3, 'Samuel - 17 - München')
pm.commit(transaction_id)

transaction_id = pm.begin_transaction()
pm.write(transaction_id, 4, 'Frank - 17 - München')
pm.write(transaction_id, 5, 'Benny - 17 - München')
pm.write(transaction_id, 6, 'Rick - 17 - München')
pm.commit(transaction_id)
time.sleep(20)
