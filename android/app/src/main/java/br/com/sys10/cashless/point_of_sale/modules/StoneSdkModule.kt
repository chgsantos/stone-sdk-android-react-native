package br.com.sys10.cashless.example.modules

import android.content.Context
import br.com.stone.posandroid.paymentapp.deeplink.PaymentDeeplink
import br.com.stone.posandroid.paymentapp.deeplink.model.InstallmentType
import br.com.stone.posandroid.paymentapp.deeplink.model.PaymentInfo
import br.com.stone.posandroid.paymentapp.deeplink.model.TransactionType
import com.facebook.react.bridge.*
import stone.application.StoneStart
import stone.application.interfaces.StoneCallbackInterface
import stone.database.transaction.TransactionDAO
import stone.database.transaction.TransactionObject
import stone.providers.CancellationProvider
import stone.providers.ReversalProvider
import stone.utils.Stone


class StoneSdkModule (reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
  private val applicationContext: Context = reactContext.applicationContext

  override fun getName(): String {
    return "StoneSdkModule"
  }

  init {
    StoneStart.init(applicationContext)
    Stone.setAppName("Beba")
  }

  @ReactMethod
  fun getSerialNumber(promise: Promise) {
    val serial = Stone.getPosAndroidDevice().getPosAndroidSerialNumber()
    promise.resolve(serial)
  }

  @ReactMethod
  fun sendPaymentDeeplink(returnScheme: String?, amount: Int, transactionType: String?, orderId: String?, installmentType: String?, installmentCount: String?, editableAmount: Boolean) {

    var transactionTypeFormatted: TransactionType? = null
    when (transactionType) {
      "CREDIT" -> transactionTypeFormatted = TransactionType.CREDIT
      "DEBIT" -> transactionTypeFormatted = TransactionType.DEBIT
      "PIX" -> transactionTypeFormatted = TransactionType.PIX
      "VOUCHER" -> transactionTypeFormatted = TransactionType.VOUCHER
    }

    val orderIdFormatted: Long? = orderId?.toLong()

    var installmentTypeFormatted: InstallmentType? = null
    when (installmentType) {
      "ISSUER" -> installmentTypeFormatted = InstallmentType.ISSUER
      "MERCHANT" -> installmentTypeFormatted = InstallmentType.MERCHANT
      "NONE" -> installmentTypeFormatted = InstallmentType.NONE
    }

    val installmentCountFormatted: Int? = installmentCount?.toInt()

    val paymentInfo = PaymentInfo(
      amount.toLong(),
      transactionTypeFormatted,
      installmentCountFormatted,
      orderIdFormatted,
      editableAmount,
      returnScheme,
      installmentTypeFormatted
    )

    val paymentDeeplink = PaymentDeeplink(applicationContext)
    paymentDeeplink.sendDeepLink(paymentInfo)
  }

  @ReactMethod
  fun cancelTransactionWithAtk(atk: String, promise: Promise) {
    val transactionDAO = TransactionDAO(applicationContext)
    val transaction = transactionDAO.findTransactionWithAtk(atk)
    cancelTransaction(transaction, promise)
  }

  @ReactMethod
  fun cancelTransactionWithAuthorizationCode(code: String, promise: Promise) {
    val transactionDAO = TransactionDAO(applicationContext)
    val transaction = transactionDAO.findTransactionWithAuthorizationCode(code)
    cancelTransaction(transaction, promise)
  }

  @ReactMethod
  fun cancelTransactionWithId(transactionId: Int, promise: Promise) {
    val transactionDAO = TransactionDAO(applicationContext)
    val transaction = transactionDAO.findTransactionWithId(transactionId)
    cancelTransaction(transaction, promise)
  }

  @ReactMethod
  fun cancelTransactionWithInitiatorTransactionKey(key: String, promise: Promise) {
    val transactionDAO = TransactionDAO(applicationContext)
    val transaction = transactionDAO.findTransactionWithInitiatorTransactionKey(key)
    cancelTransaction(transaction, promise)
  }

  @ReactMethod
  fun getAllTransactions(promise: Promise) {
    val transactionDAO = TransactionDAO(applicationContext)
    val result = transactionDAO.getAllTransactions()
    promise.resolve(transactionListToWritableArray(result))
  }

  @ReactMethod
  fun getAllTransactionsOrderByIdDesc(promise: Promise) {
    val transactionDAO = TransactionDAO(applicationContext)
    val result = transactionDAO.getAllTransactionsOrderByIdDesc()
    promise.resolve(transactionListToWritableArray(result))
  }

  @ReactMethod
  fun getLastTransactionId(promise: Promise) {
    val transactionDAO = TransactionDAO(applicationContext)
    val result = transactionDAO.getLastTransactionId()
    promise.resolve(result)
  }

  @ReactMethod
  fun findTransactionByFilter(key: String, value: String, promise: Promise) {
    val transactionDAO = TransactionDAO(applicationContext)
    val result = transactionDAO.findTransactionByFilter(mapOf(key to value))
    promise.resolve(transactionListToWritableArray(result))
  }

  @ReactMethod
  fun findTransactionWithAtk(atk: String, promise: Promise) {
    val transactionDAO = TransactionDAO(applicationContext)
    val result = transactionDAO.findTransactionWithAtk(atk)
    if (result != null) {
      promise.resolve(transactionToWritableMap(result))
    } else {
      promise.reject("Transaction não encontrada.")
    }
  }

  @ReactMethod
  fun findTransactionWithAuthorizationCode(code: String, promise: Promise) {
    val transactionDAO = TransactionDAO(applicationContext)
    val result = transactionDAO.findTransactionWithAuthorizationCode(code)
    if (result != null) {
      promise.resolve(transactionToWritableMap(result))
    } else {
      promise.reject("Transaction não encontrada.")
    }
  }

  @ReactMethod
  fun findTransactionWithId(transactionId: Int, promise: Promise) {
    val transactionDAO = TransactionDAO(applicationContext)
    val result = transactionDAO.findTransactionWithId(transactionId)
    if (result != null) {
      promise.resolve(transactionToWritableMap(result))
    } else {
      promise.reject("Transaction não encontrada.")
    }
  }

  @ReactMethod
  fun findTransactionWithInitiatorTransactionKey(key: String, promise: Promise) {
    val transactionDAO = TransactionDAO(applicationContext)
    val result = transactionDAO.findTransactionWithInitiatorTransactionKey(key)
    if (result != null) {
      promise.resolve(transactionToWritableMap(result))
    } else {
      promise.reject("Transaction não encontrada.")
    }
  }

  @ReactMethod
  fun reversalProviderCallback(promise: Promise) {
    val reversalProvider = ReversalProvider(applicationContext)
    reversalProvider.dialogMessage = "Cancelando transação com erro"
    reversalProvider.isDefaultUI
    reversalProvider.connectionCallback = object : StoneCallbackInterface {
      override fun onSuccess() {
        promise.resolve(true)
      }

      override fun onError() {
        promise.reject("Error")
      }
    }
    reversalProvider.execute()
  }

  private fun cancelTransaction(transaction: TransactionObject?, promise: Promise) {
    if (transaction == null) {
      promise.reject("Transaction não encontrada.")
      return
    }

    val cancellationProvider = CancellationProvider(applicationContext, transaction)
    cancellationProvider.connectionCallback = object : StoneCallbackInterface {
      override fun onSuccess() {
        promise.resolve(true)
      }

      override fun onError() {
        promise.reject("Error")
      }
    }
    cancellationProvider.execute()
  }

  private fun transactionToWritableMap(transaction: TransactionObject): WritableMap {
    val writableMap = Arguments.createMap()

    writableMap.putString("acquirerTransactionKey", transaction.acquirerTransactionKey)
    writableMap.putString("initiatorTransactionKey", transaction.initiatorTransactionKey)
    writableMap.putString("amount", transaction.amount)
    writableMap.putString("typeOfTransaction", transaction.typeOfTransaction.toString())
    writableMap.putString("instalmentTransaction", transaction.instalmentTransaction.toString())
    writableMap.putString("instalmentType", transaction.instalmentType.toString())
    writableMap.putString("cardHolderNumber", transaction.cardHolderNumber)
    writableMap.putInt("cardBrandId", transaction.cardBrandId)
    writableMap.putString("cardBrandName", transaction.cardBrandName)
    writableMap.putString("cardHolderName", transaction.cardHolderName)
    writableMap.putString("authorizationCode", transaction.authorizationCode)
    writableMap.putString("transactionStatus", transaction.transactionStatus.toString())
    writableMap.putString("date", transaction.date)
    writableMap.putString("shortName", transaction.shortName)
    writableMap.putString("pinpadUsed", transaction.pinpadUsed)
    writableMap.putString("balance", transaction.balance)
    writableMap.putString("isCapture", transaction.isCapture.toString())
    writableMap.putString("subMerchantCategoryCode", transaction.subMerchantCategoryCode)
    writableMap.putString("subMerchantAddress", transaction.subMerchantAddress)
    writableMap.putString("externalId", transaction.externalId)
    writableMap.putString("requestId", transaction.requestId)

    return writableMap
  }

  private fun transactionListToWritableArray(transactions: List<TransactionObject>): WritableArray {
    val writableArray = Arguments.createArray()
    transactions.forEach { result ->
      writableArray.pushMap(transactionToWritableMap(result))
    }
    return writableArray
  }
}
