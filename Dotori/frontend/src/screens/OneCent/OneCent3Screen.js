import React from 'react';
import { 
  View,
  Text,
  Image,
  StyleSheet,
  TouchableOpacity } from 'react-native';


export default function OneCent1Screen({ route }){
  const { accountNumber, navigation } = route.params; // 계좌 번호를 전달받음

  const handleConfirm = () => {
    // 이 부분에 1원 보내기를 처리하는 코드를 추가할 수 있습니다.

    // 페이지 이동 예시:
    navigation.navigate('OneCent4Screen');
  };

  return (
    <View style={styles.container}>
      <Image
        style={styles.image}
        source={require('../../assets/images/Hamster/LightHamster.png')}
      />

      <View style={styles.box}>
        <Image
          style={styles.leftImage}
          source={require('../../assets/images/logo_podo.png')}
        />
        <Text style={styles.text}>{accountNumber}</Text>
      </View>

      <Text style={styles.text}>
        시민주님 계좌가 맞는지
      </Text>
      <Text style={styles.text}>확인하기 위해</Text>
      <Text style={{ fontSize: 18 }}>
        <Text style={{ color: '#FF965C' }}>1원</Text>을 보내볼께요
      </Text>
    
      <TouchableOpacity style={styles.button} onPress={handleConfirm}>
        <Text style={styles.buttonText}>1원 보내기</Text>
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor:"white",
  },
  image: {
    width: 150,
    height: 150,
  },
  box: {
    width: '60%',
    backgroundColor: '#FFFFFF',
    borderRadius: 13,
    elevation: 4,
    padding: 8,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 50
  },
  leftImage: {
    width: 50,
    height: 50,
  },
  text: {
    fontSize: 18,
    color: '#000000',
  },
  button: {
    backgroundColor: '#FF965C',
    borderRadius: 8,
    width: '80%',
    padding: 16,
    marginTop: 100,
  },
  buttonText: {
    fontSize: 18,
    color: '#FFFFFF',
    textAlign: 'center',
  },
});
