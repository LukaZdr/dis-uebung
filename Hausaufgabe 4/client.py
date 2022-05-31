from persistance_manager import PersistanceManager
import string
import random
import time

class Client():
  def __init__(self, start_page, end_page):
    self.start_page = start_page
    self.end_page = end_page
    self.pm = PersistanceManager()

  def execute_transactions(self):
    operation_count = random.randint(5, 10)
    trans_id = self.pm.begin_transaction()
    for _ in range(1, operation_count):
      page_id = random.randint(self.start_page, self.end_page)
      self.pm.write(trans_id, page_id, self.get_random_string(10))
      time.sleep(3)
    self.pm.commit(trans_id)

  def get_random_string(self, length):
    letters = string.ascii_lowercase
    result_str = ''.join(random.choice(letters) for _ in range(length))
    return result_str
