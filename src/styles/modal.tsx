import { TouchableOpacity, View } from 'react-native';
import styled from 'styled-components';

export const ModalView = styled(View)`
  justify-content: center;
  align-items: center;
  background-color: white;
  border-radius: 24px;
  width: 100px;
  height: 100px;
`;

export const Background = styled(TouchableOpacity)`
  flex: 1;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.3);
`;
