
# my-mrsaccountant

## 操作介紹

這款應用程式專為多人分帳場景設計，提供高效便捷的工具來管理群組財務。用戶可以使用 LINE ID 快速登入，建立或加入分帳群組，並記錄每筆交易細節。系統會自動結算群組內的金額平衡，生成簡潔的還款建議，讓分帳流程更清晰流暢。此外，應用程式支援用戶個人收支記錄的管理，幫助用戶全方位掌握財務狀況。

主要特色：
- **群組管理**：新增群組、管理成員及分配角色。
- **交易記錄**：記錄每筆群組支出或收入，確保數據準確。
- **結算生成**：自動計算群組成員之間的應付應收金額。
- **個人財務管理**：查詢與篩選個人財務記錄。

## 啟動指令

```shell
mvn clean package
mvn spring-boot:run
docker-compose up --build
```

## Render部署

應用程式已部署至 Render：

[Mrs. Accountant - Render](https://mrsmrsaccountant.onrender.com)

## 使用技術

- **後端**：Spring Boot
- **安全性**：Spring Security、JWT 驗證
- **資料庫**：PostgreSQL（使用 JPA 進行 ORM 處理）
- **驗證機制**：LINE 登入、JWT 認證
- **部署工具**：Render
- **版本控制**：git、GitHub

GitHub Repository: [https://github.com/wu0010802/mrs-accountant.git](https://github.com/wu0010802/mrs-accountant.git)
