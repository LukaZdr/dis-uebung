class PersistanceManager(object):
  _instance = None

  def __new__(cls):
    if cls._instance is None:
      print('creating PersistanceManager')
      cls._instance = super(PersistanceManager, cls).__new__(cls)
    return cls._instance

  def __init__(self):
    self.buffer = []
    self.logfile = 'log.txt'
    self.highest_transaction_id = self.load_transaction_id()
    self.lsn = self.load_lsn()
    self.active_transactions = set()

  def load_transaction_id(self):
    logfile = open(self.logfile, 'r')
    highest_id = 0
    for line in logfile:
      id = int(line.split(', ')[1])
      if id > highest_id:
        highest_id = id
    return highest_id

  def load_lsn(self):
    logfile = open(self.logfile, 'r')
    highest_lsn = 0
    for line in logfile:
      lsn = int(line.split(', ')[0])
      if lsn > highest_lsn:
        highest_lsn = lsn
    return highest_lsn + 1

  # One text file containing one line for each log entry.
  # A log entry either consists of:
  #   LSN, transaction ID, page ID and user data
  #   LSN, transaction ID and EOT (end of transaction).
  def log(self, transaction_id, page_id='-1', data='', eot=False):
    if transaction_id not in self.active_transactions:
      raise Exception('Transaction Id does not match any running transactions')
    self.lsn += 1
    with open(self.logfile, "a+") as log:
      log.seek(0)
      if len(log.readline()) > 0:
        log.write("\n")

      # Ending transaction if EOT is true
      if eot:
        log.write(f"{self.lsn}, {transaction_id}, EOT")
      else:
        log.write(f"{self.lsn}, {transaction_id}, {page_id}, {data}")

  def write(self, transaction_id, page_id, data):
    if transaction_id not in self.active_transactions:
      raise Exception('Transaction Id does not match any running transactions')
    self.log(transaction_id, page_id=page_id, data=data)
    # one text file per page containing the log sequence number(LSN) and user data. The file name contains the page ID
    # writes the given data with the given page ID on behalf of the given transaction to the buffer.
    # If the given page already exists, its content is replaced completely by the given data.
    page_name = f'pages/page{page_id}.txt'
    page = open(page_name, 'w')
    page.write(f'{self.lsn}, {data}')
    # search for page in buffer. If exists -> replace. If does not exist -> add.
    try:
      buffer_page_index = list(map(lambda x: x['page'].name, self.buffer)).index(page_name)
      self.buffer[buffer_page_index] = { 'page': page, 'transaction_id': transaction_id }
    except ValueError:
      self.buffer.append({ 'page': page, 'transaction_id': transaction_id })
    # The buffer may contain an arbitrary number of operations but if the buffer contains more than five datasets
    # after a write operation, the persistence manager checks whether there are operations related to already
    # committed transactions. If there are such operations, then these operations are written directly to the
    # persistent storage (non-atomic)
    if len(self.buffer) > 5:
      buffer_transactions = set(map(lambda x: x['transaction_id'], self.buffer))
      commited_transactions = buffer_transactions - self.active_transactions
      for transaction_id in commited_transactions:
        self.flush(transaction_id)

  def commit(self, transaction_id):
    if transaction_id not in self.active_transactions:
      raise Exception('Transaction Id does not match any running transactions')
    self.log(transaction_id, eot=True)
    self.active_transactions.remove(transaction_id)

  def begin_transaction(cls):
    cls.highest_transaction_id += 1
    cls.active_transactions.add(cls.highest_transaction_id)
    return cls.highest_transaction_id

  def flush(self, transaction_id):
    new_buffer = []
    for operation in self.buffer:
      if operation['transaction_id'] == transaction_id:
        operation['page'].close()
      else:
        new_buffer.append(operation)
    self.buffer = new_buffer
