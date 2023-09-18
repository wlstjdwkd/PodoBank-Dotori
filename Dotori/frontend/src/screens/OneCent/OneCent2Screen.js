import React, { useState } from 'react';
import { View, Text, TextInput, TouchableOpacity, StyleSheet, ScrollView } from 'react-native';

export default function OneCent1Screen({ navigation }) {
  const [accountNumber, setAccountNumber] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  const handleConfirm = () => {
    if (accountNumber.length !== 13) {
      setErrorMessage('계좌 번호를 13자리로 입력해주세요.');
    } else {
      setErrorMessage('');
      navigation.navigate('OneCent3Screen', { accountNumber, navigation  }); // 계좌 번호를 다음 페이지로 전달
    }
  };

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Text style={styles.boldTextLeft}>계좌 번호를 입력해주세요.</Text>

      {/* 텍스트 입력 박스 */}
      <View style={styles.inputContainer}>
        <TextInput
          style={styles.input}
          placeholder="계좌 번호를 입력하세요."
          placeholderTextColor="#A9A9A9"
          underlineColorAndroid="transparent" // 하단 선 숨기기
          keyboardType="numeric" // 숫자 키패드 표시
          maxLength={13} // 최대 13자리로 제한
          textAlign="center" // 가운데 정렬
          value={accountNumber}
          onChangeText={(text) => {
            setAccountNumber(text);
            if (text.length !== 13) {
              setErrorMessage('계좌 번호를 13자리로 입력해주세요.');
            } else {
              setErrorMessage('');
            }
          }}
        />
      </View>

      {/* 오류 메시지 */}
      {errorMessage !== '' && <Text style={styles.errorMessage}>{errorMessage}</Text>}

      {/* 버튼 */}
      <TouchableOpacity style={styles.button} onPress={handleConfirm}>
        <Text style={styles.buttonText}>확인</Text>
      </TouchableOpacity>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'flex-start',
    justifyContent: 'center',
    backgroundColor: 'white',
    padding: 16,
  },
  boldTextLeft: {
    fontWeight: 'bold',
    fontSize: 18,
    marginBottom: 8,
    textAlign: 'left',
  },
  inputContainer: {
    width: '100%',
    borderBottomWidth: 1, // 하단 선 추가
    borderColor: '#FF965C', // 선 색상 설정
    marginBottom: 50,
  },
  input: {
    width: '100%',
    fontSize: 16,
    paddingVertical: 8,
    color: '#000000',
  },
  button: {
    backgroundColor: '#FF965C',
    borderRadius: 8,
    width: '100%',
    padding: 16,
    alignItems: 'center',
  },
  buttonText: {
    fontSize: 18,
    color: 'white',
    fontWeight: 'bold',
  },
  errorMessage: {
    color: 'red',
    fontSize: 16,
    textAlign: 'center',
    marginTop: 8,
  },
});
