import sqlite3
import mysql.connector # type: ignore

# Kết nối tới SQLite
sqlite_conn = sqlite3.connect('C:/Users/dlmin/OneDrive/Máy tính/English-App/APIEnglishApp/src/main/resources/db/dict_hh.db')
sqlite_cursor = sqlite_conn.cursor()

# Kết nối tới MySQL
mysql_conn = mysql.connector.connect(
    host='localhost',
    user='root',
    password='minhanh2722004',
    database='english-app'
)
mysql_cursor = mysql_conn.cursor()

# Lấy danh sách các bảng từ SQLite
sqlite_cursor.execute("SELECT name FROM sqlite_master WHERE type='table';")
tables = sqlite_cursor.fetchall()

# Chuyển dữ liệu bảng từ SQLite sang MySQL
def transfer_table_data(table_name):
    # Lấy cấu trúc bảng từ SQLite
    sqlite_cursor.execute(f"PRAGMA table_info({table_name})")
    columns = sqlite_cursor.fetchall()
    column_names = [col[1] for col in columns]

    # Tạo câu lệnh CREATE TABLE cho MySQL
    create_table_sql = f"CREATE TABLE IF NOT EXISTS {table_name} ({', '.join([f'{col[1]} {map_sqlite_type_to_mysql(col[2])}' for col in columns])});"
    mysql_cursor.execute(create_table_sql)

    # Lấy dữ liệu từ bảng SQLite
    sqlite_cursor.execute(f"SELECT * FROM {table_name}")
    rows = sqlite_cursor.fetchall()

    # Chèn dữ liệu vào MySQL
    for row in rows:
        placeholders = ', '.join(['%s'] * len(row))
        insert_sql = f"INSERT INTO {table_name} ({', '.join(column_names)}) VALUES ({placeholders})"
        mysql_cursor.execute(insert_sql, row)
    mysql_conn.commit()

# Map các kiểu dữ liệu từ SQLite sang MySQL
def map_sqlite_type_to_mysql(sqlite_type):
    sqlite_type = sqlite_type.lower()
    if 'int' in sqlite_type:
        return 'INT'
    elif 'text' in sqlite_type:
        return 'TEXT'
    elif 'real' in sqlite_type or 'numeric' in sqlite_type:
        return 'FLOAT'
    elif 'blob' in sqlite_type:
        return 'BLOB'
    else:
        return 'TEXT'

# Chuyển dữ liệu cho từng bảng
for table in tables:
    table_name = table[0]
    print(f"Đang chuyển bảng: {table_name}")
    transfer_table_data(table_name)

# Đóng kết nối
sqlite_conn.close()
mysql_conn.close()

print("Chuyển đổi hoàn tất.")