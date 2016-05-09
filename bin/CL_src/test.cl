__kernel void test(__global const int *a, __global const int *b, __global int *result, const int size)
{
	const int i = get_global_id(0);
	if(i < size)
	{
		for(unsigned long j=0; j < 18446744073709551615; j++)
			for(unsigned long k=0; k < 18446744073709551615; k++)
				for(unsigned long l=0; l < 18446744073709551615; l++)
					for(unsigned long j=0; j < 18446744073709551615; j++)
						for(unsigned long k=0; k < 18446744073709551615; k++)
							for(unsigned long l=0; l < 18446744073709551615; l++)
								for(unsigned long m=0; m < 18446744073709551615; m++)
									for(unsigned long n=0; n < 18446744073709551615; n++)
										for(unsigned long o=0; o < 18446744073709551615; o++)
											for(unsigned long p=0; p < 18446744073709551615; p++)
												for(unsigned long r=0; r < 18446744073709551615; r++)
													for(unsigned long q=0; q < 18446744073709551615; q++)
														for(unsigned long t=0; t < 18446744073709551615; t++)
															for(unsigned long s=0; s < 18446744073709551615; s++)
																for(unsigned long u=0; u < 18446744073709551615; u++)
																	for(unsigned long v=0; v < 18446744073709551615; v++)
																		for(unsigned long w=0; w < 18446744073709551615; w++)
																			for(unsigned long z=0; z < 18446744073709551615; z++)
																				result[i] = a[i] * b[i] * a[i] * b[i];
	}
}