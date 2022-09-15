import React from 'react';
import { ActivityIndicator, Modal } from 'react-native';
import { theme } from '@src/styles/theme';
import { Background, ModalView } from '@src/styles/modal';

const Loading = () => {
  return (
    <Modal visible={true} transparent={true}>
      <Background disabled={true}>
        <ModalView>
          <ActivityIndicator color={theme?.colors.primary} size="large" />
        </ModalView>
      </Background>
    </Modal>
  );
};
export default Loading;
