from persistance_manager import PersistanceManager

from persistance_manager import PersistanceManager

class RecoveryTool():
  def __init__(self):
    self.logfile = 'log.txt'
    self.pm = PersistanceManager()

  def run(self):
    logs = open(self.logfile)
    lines = logs.readlines()

    # ANALYSIS
    # declare winner transactions
    winner_transactions = []
    for log in lines:
      values = log.rstrip('\n').split(', ')
      if values[-1] == 'EOT':
        winner_transactions.append(values[1]) # adds tranaction_id to winners

    # REDO
    for log in lines:
      values = log.rstrip('\n').split(', ')
      if values != [''] and values[-1] != 'EOT':
        lsn, trans_id, page_id, data  = values
        if trans_id in winner_transactions:
          page = open(f"pages/page{page_id}.txt")
          user_data = page.read()
          if user_data != '':
            page_lsn, _ = user_data.split(', ')
            if int(lsn) > int(page_lsn):
              with open(f"pages/page{page_id}.txt", 'w+') as p:
                p.write(f"{lsn}, {data}")

          # if int(lsn) > page_lsn:
          #   page.write(f"{lsn}, {data}")
          # else:
          #   log
