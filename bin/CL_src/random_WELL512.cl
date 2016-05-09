__kernel void random_WELL512(__global long *state, __global int *index)
{
	// a random number generator WELL512
	unsigned long a, b, c, d;
	a = state[index[0]];
	c = state[(index[0]+13)&15];
	b = a^c^(a<<16)^(c<<15);
	c = state[(index[0]+9)&15];
	c ^= (c>>11);
	a = state[index[0]] = b^c;
	d = a^((a<<5)&0xDA442D24UL);
	index[0] = (index[0] + 15)&15;
	a = state[index[0]];
	state[index[0]] = a^b^d^(a<<2)^(b<<18)^(c<<28);
}