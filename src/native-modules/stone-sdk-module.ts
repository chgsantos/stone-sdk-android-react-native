import { NativeModules } from 'react-native';

export interface IStoneSdkModule {
  getSerialNumber(): Promise<string>;

  sendPaymentDeeplink(
    returnScheme: string | null,
    amount: number,
    transactionType: string | null,
    orderId: string | null,
    installmentType: string | null,
    installmentCount: string | null,
    editableAmount: boolean
  ): void;

  cancelTransactionWithAtk(atk: string): Promise<boolean>;

  cancelTransactionWithAuthorizationCode(code: string): Promise<boolean>;

  cancelTransactionWithId(transactionId: number): Promise<boolean>;

  cancelTransactionWithInitiatorTransactionKey(key: string): Promise<boolean>;

  getAllTransactions(): Promise<any[]>;

  getAllTransactionsOrderByIdDesc(): Promise<any[]>;

  getLastTransactionId(): Promise<number>;

  findTransactionByFilter(key: string, value: string): Promise<any[]>;

  findTransactionWithAtk(atk: string): Promise<any>;

  findTransactionWithAuthorizationCode(code: string): Promise<any>;

  findTransactionWithId(transactionId: number): Promise<any>;

  findTransactionWithInitiatorTransactionKey(key: string): Promise<any>;

  reversalProviderCallback(): Promise<any>;
}

export default NativeModules.StoneSdkModule as IStoneSdkModule;

export const SimpleSendPaymentDeeplink = (
  returnScheme: string,
  amount: number,
  transactionType: string,
  orderId: string
) => {
  NativeModules.StoneSdkModule.sendPaymentDeeplink(
    returnScheme,
    amount,
    transactionType,
    orderId,
    'NONE',
    null,
    false
  );
};
