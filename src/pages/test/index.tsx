import React, { useEffect, useState } from 'react';
import { SafeAreaView, ScrollView, View } from 'react-native';
import { Text, Button } from 'react-native-paper';
import StoneSdkModule, { SimpleSendPaymentDeeplink } from '@native/stone-sdk-module';
import * as Linking from 'expo-linking';
import { STONE_TRANSACTION_TYPE } from '@constants/stone-constants';

const TestPage = () => {
  const [data, setData] = useState<any>(null);

  useEffect(() => {
    const handlerLinkingUrl: Linking.URLListener = async ({ url }) => {
      console.log('handlerLinkingUrl', url);
      setData(url);
    };

    Linking.addEventListener('url', handlerLinkingUrl);

    return () => {
      Linking.removeEventListener('url', handlerLinkingUrl);
    };
  }, []);

  const TestSimpleSendPaymentDeeplink = async () => {
    SimpleSendPaymentDeeplink('cashless.add-balance', 1000, STONE_TRANSACTION_TYPE.CREDIT, '123');
    setData(true);
  };

  const TestGetAllTransactions = async () => {
    const response = await StoneSdkModule.getAllTransactions();
    setData(response);
  };

  const Clear = async () => {
    setData(null);
  };

  return (
    <SafeAreaView style={{ flex: 1 }}>
      <View style={{ padding: 10, flexDirection: 'column', flex: 1 }}>
        <View>
          <Button
            style={{ marginBottom: 10 }}
            mode="contained"
            onPress={TestSimpleSendPaymentDeeplink}
          >
            SimpleSendPaymentDeeplink
          </Button>

          <Button style={{ marginBottom: 10 }} mode="contained" onPress={TestGetAllTransactions}>
            TestGetAllTransactions
          </Button>

          <Button style={{ marginBottom: 10 }} mode="contained" onPress={Clear}>
            Clear
          </Button>
        </View>

        <ScrollView style={{ borderColor: 'red', borderWidth: 1 }}>
          <Text>{JSON.stringify(data, null, 4)}</Text>
        </ScrollView>
      </View>
    </SafeAreaView>
  );
};

export default TestPage;
